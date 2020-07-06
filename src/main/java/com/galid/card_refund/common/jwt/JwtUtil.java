package com.galid.card_refund.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private long expiration;
    private SecretKey key;

    public JwtUtil() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.expiration = 3600 * 1000;
    }

    public String generateToken() {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public void validateToken(String token) {
        Jws<Claims> parsedToken = parseToken(token);
        Claims tokenBody = parsedToken.getBody();
        validateExpired(tokenBody.getExpiration());
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    private void validateExpired(Date expiration) {
        Date now = new Date();
        if(now.after(expiration))
            throw new IllegalStateException("Token이 만료되었습니다.");
    }
}
