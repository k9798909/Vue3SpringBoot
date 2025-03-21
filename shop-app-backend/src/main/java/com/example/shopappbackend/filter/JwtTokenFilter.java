package com.example.shopappbackend.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.shopappbackend.utils.JwtTokenUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get jwt token and validate
        final Optional<String> token = getValidJwtTokenFromHeader(request);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        String username = jwtTokenUtils.getUsername(token.get());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                new ArrayList<>());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    /**
     * 從header取得token，token過期或不合法回傳empty
     *
     * @param request HttpServletRequest
     */
    private Optional<String> getValidJwtTokenFromHeader(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtils.validateToken(token)) {
            return Optional.empty();
        }

        return Optional.of(token);
    }

}
