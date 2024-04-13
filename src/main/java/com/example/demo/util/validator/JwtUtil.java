package com.example.demo.util.validator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY =
            "mj5UiXDGeiUWpdAaVg8aS4UOYa2Pj5Pi";
    private static final long EXPIRED_AT = 5 * 60 * 1000; // 5 min (Millisecond)
    private static final long EXPIRED_RT = 7 * 24 * 60 * 60 * 1000; // 7 days (Millisecond)

    public static String createAccessToken(UserDetails userDetails) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + EXPIRED_AT);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .issuer(userDetails.getUsername())
                .issuedAt(currentDate)
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    public static String createRefreshToken() {
        return null;
    }

    public static boolean isAccessTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static boolean isValidAccessToken(String token, UserDetails userDetails) {
        if (userDetails.getUsername().equals(extractUsername(token)) && !isAccessTokenExpired(token))
            return true;
        return false;
    }

    public static SecretKey getKey() {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return key;
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public static String extractUsername(String token) {
        return extractAllClaims(token).getIssuer();
    }
}
