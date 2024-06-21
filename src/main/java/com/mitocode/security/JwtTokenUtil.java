package com.mitocode.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.util.stream.Collectors.joining;

//Clase S1
@Component
public class JwtTokenUtil implements Serializable {

    //milisegundos
    public final long JWT_TOKEN_VALIDITY = 5L * 60 * 60 * 1000;//5 horas

    @Value("${jwt.secret}") //EL Expression Language
    private String secret;

    public String generateToken(UserDetails userDetails){
        var claims = new HashMap<String, Object>();
        claims.put("role", userDetails.getAuthorities().stream()
                                                       .map(GrantedAuthority::getAuthority)
                                                       .collect(joining()));
        claims.put("test", "value test");
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(secret), HS512.getJcaName());
    }

    //utils
    public Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails){
        var username = getUsernameFromToken(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }
}