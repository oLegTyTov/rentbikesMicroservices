package com.example.bikemicroservice.configs;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bikemicroservice.dtos.UserSecurity;
import com.example.bikemicroservice.exceptions.FilterException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${urlGetUsernameByAccessToken}")
    private String urlGetUsernameByAccessToken;
    @Value("${urlGetUserSecurityByUsername}")
    private String urlGetUserSecurityByUsername;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws jakarta.servlet.ServletException, java.io.IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String accessToken = null;

        // Перевіряємо наявність токена в заголовку Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
            try {
                username = restTemplate.getForEntity(urlGetUsernameByAccessToken + "/" + accessToken, String.class)
                        .getBody();
                UserSecurity userSecuity = restTemplate
                        .getForEntity(urlGetUserSecurityByUsername + "/" + username, UserSecurity.class).getBody();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userSecuity, null, null);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } catch (HttpClientErrorException e) {
                throw new FilterException();
            }
            // Продовжуємо ланцюг фільтрів
            chain.doFilter(request, response);
        }
    }
}