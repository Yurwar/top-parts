package com.topparts.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    @Value("${security.jwt.header}")
    private String jwtHeaderName = "Authorization";
    @Value("${security.jwt.prefix}")
    private String jwtPrefixName = "Bearer";
    @Value("${security.jwt.secret}")
    private String jwtSecret = "JwtSecretKey";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(jwtHeaderName);
        if (Objects.isNull(header) || !header.startsWith(jwtPrefixName)) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(jwtPrefixName, "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            if (Objects.nonNull(username)) {
                List<String> authorities = parseAuthorities(claims);

                List<SimpleGrantedAuthority> grantedAuthorities = authorities
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,
                        null,
                        grantedAuthorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Failed to authenticate user");
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private List<String> parseAuthorities(Claims claims) {
        List rawAuthorities = claims.get("authorities", List.class);

        List<String> authorities = new ArrayList<>();

        for (Object rawAuth : rawAuthorities) {
            if (rawAuth instanceof String) {
                authorities.add((String) rawAuth);
            }
        }
        return authorities;
    }
}
