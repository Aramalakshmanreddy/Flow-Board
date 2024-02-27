package com.project.authentication.security;

import com.project.authentication.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecurityTokenGenerateImpl implements ISecurityTokenGenerate
{

    @Override
    public String createToken(User user) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("Email", user.getEmail());
        claims.put("UserName", user.getUserName());
        return generateToken(claims, user.getEmail());
    }

    public String generateToken(Map<String,Object> claims, String subject) {
        long expirationInMillis = System.currentTimeMillis() + (3600*1000);     //3600000 is equal for 1hr
        // Generate the token and set the user id in the claims
        String jwtToken = Jwts.builder()
                .setIssuer("Kanban Board Service")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationInMillis)) //this will provide Token Expired Time
                .signWith(SignatureAlgorithm.HS256,"PrivateKey")
                .compact();
        return jwtToken;
    }
}
