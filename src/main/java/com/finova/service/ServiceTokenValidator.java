package com.finova.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class ServiceTokenValidator {

    private final SecretKey secretKey;

    public ServiceTokenValidator(
            @Value("${service.customer-service.secret}")
            String secret
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public boolean isValid(String token) {

        try {

            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            System.out.println("SUBJECT: " + claims.getSubject());
            System.out.println(
                    "SERVICE: " + claims.get("service", String.class)
            );
            System.out.println(
                    "TYPE: " + claims.get("type", String.class)
            );

            return "customer-service".equals(claims.getSubject())
                    && "customer-service".equals(
                    claims.get("service", String.class))
                    && "service".equals(
                    claims.get("type", String.class));

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }

    }
}