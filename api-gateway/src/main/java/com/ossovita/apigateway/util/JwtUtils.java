package com.ossovita.apigateway.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Claims getClaims(final String token){
        try{
            Claims body = Jwts.parser().setSigningKey(jwtSecret)
                    .parseClaimsJws(token).getBody();
            return body;
        }catch (Exception e){
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT signature: {}", e.getMessage());
            throw new SignatureException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT token: {}", e.getMessage());
            throw new MalformedJwtException(e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is expired: {}", e.getMessage());
            throw new ExpiredJwtException(null,null,e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is unsupported: {}", e.getMessage());
            throw new UnsupportedJwtException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JwtUtils | validateJwtToken | JWT claims string is empty: {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }

    }







}
