package com.archive.ruleservice.config;

import com.archive.ruleservice.model.UserRoles;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final String secretKey = "SecretKeyGenerator";
    private final long tokenValidity = 3600000;

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
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
                 | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
