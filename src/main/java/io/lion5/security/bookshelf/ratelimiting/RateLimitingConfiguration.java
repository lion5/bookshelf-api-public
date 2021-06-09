package io.lion5.security.bookshelf.ratelimiting;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class RateLimitingConfiguration {

    private final long unauthenticatedLimit;
    private final Duration unauthenticatedRefill;
    private final long authenticatedLimit;
    private final Duration authenticatedRefill;

    public RateLimitingConfiguration(@Value("${ratelimiting.unauthenticated.limit}") long unauthenticatedLimit,
                                     @Value("${ratelimiting.unauthenticated.refill}") String unauthenticatedRefill,
                                     @Value("${ratelimiting.authenticated.limit}") long authenticatedLimit,
                                     @Value("${ratelimiting.authenticated.refill}") String authenticatedRefill) {
        this.unauthenticatedLimit = unauthenticatedLimit;
        this.unauthenticatedRefill = Duration.parse(unauthenticatedRefill);
        this.authenticatedLimit = authenticatedLimit;
        this.authenticatedRefill = Duration.parse(authenticatedRefill);
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(this));
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }

    public long getUnauthenticatedLimit() {
        return unauthenticatedLimit;
    }

    public Duration getUnauthenticatedRefill() {
        return unauthenticatedRefill;
    }

    public long getAuthenticatedLimit() {
        return authenticatedLimit;
    }

    public Duration getAuthenticatedRefill() {
        return authenticatedRefill;
    }

}
