package com.topparts.security.config;

import com.topparts.security.filter.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
    @Value("${security.jwt.uri}")
    private String jwtUri = "/login/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtUri).permitAll()
                .antMatchers("/api/product/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/product").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/product/*").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/orders").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/orders/*").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/orders/users/*").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/orders").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/discovery/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();

    }
}
