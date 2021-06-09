package io.lion5.security.bookshelf.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configures Spring Security.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2ResourceServer(oauth -> oauth.jwt().jwtAuthenticationConverter(AwsCognitoAuthentication::new))
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/books").permitAll()
            .antMatchers(HttpMethod.GET, "/books/*").permitAll()
            .anyRequest().authenticated();
    }
}
