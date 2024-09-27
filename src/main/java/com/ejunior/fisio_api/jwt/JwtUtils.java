package com.ejunior.fisio_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "48923rji-23fj802g324-3yug42e24fh";
    public static final long EXPIRE_MINUTES = 60;

    private JwtUtils(){}

    private static SecretKey generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start){
        long converterToMinutes = EXPIRE_MINUTES * 60000;
        return new Date(start.getTime() + converterToMinutes);
    }

    public static JwtToken createToken(String username, String role){
        Date issueAt = new Date();
        Date limit = toExpireDate(issueAt);
        String token = Jwts.builder().header().add("typ", "JWT")
                .and().subject(username)
                .issuedAt(issueAt)
                .expiration(limit)
                .signWith(generateKey()).claim("role", role).compact();
        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token){
        try{
            return Jwts.parser().verifyWith(generateKey()).build()
                    .parseSignedClaims(refactorToken(token)).getPayload();

        }catch (JwtException ex){
            System.out.println("Token inválido ");
        }
        return null;
    }

    private static CharSequence refactorToken(String token){
        if(token.contains(JWT_BEARER)){
            return token.substring((JWT_BEARER.length()));
        }
        return token;
    }

    public static String getUsernameFromToken(String token){
        return Objects.requireNonNull(getClaimsFromToken(token)).getSubject();
    }

    public static boolean isTokenValid(String token){
        try{
            Jwts.parser().verifyWith(generateKey()).build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException ex){
            System.out.println("Token inválido ");
        }
        return false;
    }

}
