package com.finpay.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${spring.security.jwt.secret-key}")
    private String secret;

    @Value("${spring.security.jwt.issuer}")
    private String issuer;

    @Value("${spring.security.jwt.expiry-time-in-seconds}")
    private Long expiryTimeInSeconds;

    public String createToken(Authentication auth){
        CustomUserDetails user=(CustomUserDetails) auth.getPrincipal();
        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder().issuer(issuer).subject(user.getUsername()).claim("UserId",user.getId().toString()).claim("authorities",authorities).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + (expiryTimeInSeconds * 1000))).signWith(getSecretKey()).compact();
    }

    public Authentication getAuthentication(String token) {
        Claims payload = parseClaimsFromToken(token);
        String username = payload.getSubject();

        @SuppressWarnings("unchecked")
        List<String> authorities = payload.get("authorities", List.class);

        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(username, "", grantedAuthorities);
    }


    public boolean validateToken(String token) {
        try {
            parseClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Also catching IllegalArgumentException in case token is null/empty
            return false;
        }
    }

    private Claims parseClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        // NEW: Decoding a Base64 string from application.yml
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
