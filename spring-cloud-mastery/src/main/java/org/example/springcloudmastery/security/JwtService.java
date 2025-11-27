package org.example.springcloudmastery.security;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.springcloudmastery.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final Key secretKey;

    public JwtService(@Value("${jwt.secret:}") String secret) {
        if (secret == null || secret.isBlank()) {
            secret = System.getenv("JWT_SECRET");
        }
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Missing jwt.secret (Config Server or env JWT_SECRET).");
        }
        this.secretKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secret));
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    public Role extractRole(String token){
        return Role.valueOf(extractClaim(token, claims -> claims.get("role",String.class)));
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}
