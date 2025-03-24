package com.example.shopappbackend.utils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    private final Key secretKey;
    private final long expirationTime;

    @Autowired
    public JwtTokenUtils(
            @Value("${jwt.expiration.time}") long expirationTime,
            @Value("${jwt.secret.key:1hGfRtZsL7pOqXyW9uBvNc6mKjEiAx3d2gVfRtZsL7pOqXyW9uBvNc6mKjEiAx3d2gVfRtZsL7pOqX}") String secretKey) {
        this.expirationTime = expirationTime;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(Instant.now().toEpochMilli() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getOrDefault("username", "").toString();
        } catch (Exception e) {
            log.debug(e.getMessage());
            return "";
        }
    }
}
