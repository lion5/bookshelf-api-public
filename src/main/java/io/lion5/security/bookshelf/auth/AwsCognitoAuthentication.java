package io.lion5.security.bookshelf.auth;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class AwsCognitoAuthentication extends AbstractAuthenticationToken {

    private final Jwt jwt;

    public AwsCognitoAuthentication(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }

    @Override
    public String getName() {
        return jwt.getClaimAsString("username");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AwsCognitoAuthentication that = (AwsCognitoAuthentication) o;
        return Objects.equals(jwt, that.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), jwt);
    }
}
