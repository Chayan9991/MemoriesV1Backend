package com.project.jwt;

import com.project.entity.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final String SECRET_KEY = "0682953d697461dee8c5ee9cefcc6856190d01982dcea61332dbcb81c7615917";
    private long expirationTime = 1000 * 60 * 60 ; // 1 hr
    //Token Generation

    public String generateToken(UserDetails userDetails){

        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
      byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
      return Keys.hmacShaKeyFor(keyBytes);
    }

    //Extract Claims from Token

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver){
       final Claims claims = extractAllClaims(token);
       return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return getClaimsFromToken(token, Claims::getSubject);
    }

    //Validate a Token

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
          return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    // Extract Authorities (Roles) from Token

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<Map<String, String>> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.get("authority")))
                .collect(Collectors.toList());
    }


}
