package com.archive.service.config;

import com.archive.service.model.UserRoles;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret:SecretKeyGenerator}")
    private String secretKey;
    @Value("${jwt.validity:600000}")
    private long tokenValidity;

    public String generateToken(UserDetails userDetails, List<UserRoles> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidity);

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", roles);

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (SignatureException ex) {
            log.error("Exception occurred while validating token {}", ex.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
