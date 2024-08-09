package com.example.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtHelper {

    private static SecretKey secretKey = null;

    private static final int minutes = 60;

    public static String generateToken(Map<String, Object> extractClaims, String username){
        if(secretKey == null){
            try{
                KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
                secretKey = keyGenerator.generateKey();
                System.out.println("Secret Key "+secretKey);
            }catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }

        return Jwts.builder().claims(extractClaims).subject(username).issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(minutes * 60))).signWith(secretKey).compact();

    }

    public static Claims getTokenBody(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public static String extractUsername(String token){
        return getTokenBody(token).getSubject();
    }

    public static boolean isTokenExpired(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    public static boolean isTokenValid(String token, UserDetails userDetails){
        System.out.println("Inside Token Valid ============> ");
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
