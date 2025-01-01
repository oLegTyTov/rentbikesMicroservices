package com.example.securitymicroservice.utils;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
    @Value("${jwt.secret.access}")
    private String secretAccess;
    @Value("${jwt.secret.refresh}")
    private String secretRefresh;
    @Value("${jwt.secret.verification}")
    private String secretVerification;
    @Value("${jwt.expiration.access}")
    private Long expirationAccess;
    @Value("${jwt.expiration.refresh}")
    private Long expirationRefresh;
    @Value("${jwt.expiration.verification}")
    private Long expirationVerification;

    public String generateAccessToken(String username) {
        return doGenerateAccessToken(username);
    }

    public String generateRefreshToken(String username) {
        return doGenerateRefreshToken(username);
    }

    public String generateVerificationToken(String email) {
        return doGenerateVerificationToken(email);
    }

    private String doGenerateVerificationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationVerification))
                .signWith(SignatureAlgorithm.HS512, secretVerification)
                .compact();
    }

    private String doGenerateAccessToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationAccess))
                .signWith(SignatureAlgorithm.HS512, secretAccess)
                .compact();
    }

    private String doGenerateRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationRefresh))
                .signWith(SignatureAlgorithm.HS512, secretRefresh)
                .compact();
    }

    public String extractEmailOfVerificationToken(String token) {
        return extractClaimOfVerificationToken(token, Claims::getSubject);
    }

    public String extractUsernameOfAccessToken(String token) {
        return extractClaimOfAccessToken(token, Claims::getSubject);
    }

    public String extractUsernameOfRefreshToken(String token) {
        return extractClaimOfRefreshToken(token, Claims::getSubject);
    }

    public Date extractExpirationOfAccessToken(String token) {
        return extractClaimOfAccessToken(token, Claims::getExpiration);
    }   
     public Date extractExpirationOfVerificationToken(String token) {
        return extractClaimOfVerificationToken(token, Claims::getExpiration);
    }

    public Date extractExpirationOfResfreshToken(String token) {
        return extractClaimOfRefreshToken(token, Claims::getExpiration);
    }

    public <T> T extractClaimOfRefreshToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsOfRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaimOfAccessToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsOfAccessToken(token);
        return claimsResolver.apply(claims);
    }   

    public <T> T extractClaimOfVerificationToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsOfVerificationToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsOfAccessToken(String token) {
        return Jwts.parser().setSigningKey(secretAccess).parseClaimsJws(token).getBody();
    }    

    private Claims extractAllClaimsOfVerificationToken(String token) {
        return Jwts.parser().setSigningKey(secretVerification).parseClaimsJws(token).getBody();

    }

    private Claims extractAllClaimsOfRefreshToken(String token) {
        return Jwts.parser().setSigningKey(secretRefresh).parseClaimsJws(token).getBody();
    }

    private Boolean isAccessTokenExpired(String token) {
        return extractExpirationOfAccessToken(token).before(new Date());
    }   
     private Boolean isVerificationTokenExpired(String token) {
        return extractExpirationOfVerificationToken(token).before(new Date());
    }

    private Boolean isRefreshTokenExpired(String token) {
        return extractExpirationOfResfreshToken(token).before(new Date());
    }

    public Boolean validateRefreshToken(String token) {
        return !isRefreshTokenExpired(token);
    }

    public Boolean validateAccessToken(String token) {
        return !isAccessTokenExpired(token);
    }
    public Boolean validateVerificationToken(String token) {
        return !isVerificationTokenExpired(token);
    }
}
