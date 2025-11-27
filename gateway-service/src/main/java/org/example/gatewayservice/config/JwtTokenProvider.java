package org.example.gatewayservice.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenProvider {

    private static final String  SECRET_KEY = "pouaiScyrJ8GnN5hlvUBjdf7JTQQI/Ws8CMDgLmH8fs=";

    private final Key key = Keys.hmacShaKeyFor(
            java.util.Base64.getDecoder().decode(SECRET_KEY)
    );

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
        System.out.println("❌ JWT ERROR: " + e.getClass().getSimpleName() + " — " + e.getMessage());
        return false;
    }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
