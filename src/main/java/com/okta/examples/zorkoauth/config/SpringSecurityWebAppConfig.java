package com.okta.examples.zorkoauth.config;


import com.okta.examples.zorkoauth.jwt.JWTFilter;
import com.okta.examples.zorkoauth.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableOAuth2Sso
public class SpringSecurityWebAppConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenProvider tokenProvider;

    @Value("#{ @environment['okta.moreSecure.authorities'] }")
    String[] moreSecureAuthorities;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/v1/instructions").permitAll()
            .antMatchers("/v1/c").fullyAuthenticated().and()
            .csrf().ignoringAntMatchers("/v1/c").and()
            .addFilterBefore(new JWTFilter(tokenProvider), BasicAuthenticationFilter.class);
    }
}