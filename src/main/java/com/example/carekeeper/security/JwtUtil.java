package com.example.carekeeper.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey secretKey;
    private final long expirationMs = 1000 * 60 * 60; // 1h

    @Value("${jwt.secret:}")
    private String envSecret; // pega do env se existir

    @PostConstruct
    public void init() {
        if (envSecret != null && !envSecret.isEmpty()) {
            // decodifica Base64 caso seja fornecido
            byte[] keyBytes = Base64.getDecoder().decode(envSecret);
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        } else {
            // gera uma chave nova segura
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            // opcional: logar chave gerada para dev
            System.out.println("JWT secret não fornecido, chave gerada automaticamente (Base64): "
                    + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        }
    }

    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Instant getExpirationFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().toInstant();
    }
}
