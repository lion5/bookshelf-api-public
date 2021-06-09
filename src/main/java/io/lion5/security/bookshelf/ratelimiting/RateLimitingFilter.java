package io.lion5.security.bookshelf.ratelimiting;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingConfiguration configuration;

    private final Bucket unauthenticatedBucket;
    private final Bucket authenticatedBucket;

    public RateLimitingFilter(RateLimitingConfiguration configuration) {
        this.configuration = configuration;

        Refill unauthenticatedRefill = Refill.intervally(configuration.getUnauthenticatedLimit(), configuration.getUnauthenticatedRefill());
        Bandwidth unauthenticatedLimit = Bandwidth.classic(configuration.getUnauthenticatedLimit(), unauthenticatedRefill);
        unauthenticatedBucket = Bucket4j.builder()
                                        .addLimit(unauthenticatedLimit)
                                        .build();

        Refill authenticatedRefill = Refill.intervally(configuration.getAuthenticatedLimit(), configuration.getAuthenticatedRefill());
        Bandwidth authenticatedLimit = Bandwidth.classic(configuration.getAuthenticatedLimit(), authenticatedRefill);
        authenticatedBucket = Bucket4j.builder()
                .addLimit(authenticatedLimit)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (skipRateLimiting(securityContext)) {
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket = getBucket(securityContext);
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
        response.setHeader("X-RateLimit-Limit", String.valueOf(getLimit(securityContext)));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(consumptionProbe.getRemainingTokens()));

        if (!consumptionProbe.isConsumed()) {
            response.setStatus(429);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Bucket getBucket(SecurityContext context) {
        if (!isAuthenticated(context)) {
            return unauthenticatedBucket;
        }

        return authenticatedBucket;
    }

    private long getLimit(SecurityContext context) {
        if (isAuthenticated(context)) {
            return configuration.getAuthenticatedLimit();
        } else {
            return configuration.getUnauthenticatedLimit();
        }
    }

    private boolean isAuthenticated(SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName());
    }

    private boolean skipRateLimiting(SecurityContext securityContext) {
        if (!isAuthenticated(securityContext)) {
            return false;
        }

        Authentication authentication = securityContext.getAuthentication();
        return authentication.getAuthorities().stream()
                             .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
