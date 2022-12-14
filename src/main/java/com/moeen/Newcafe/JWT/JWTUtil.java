package com.moeen.Newcafe.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.function.Function;

@Service
public class JWTUtil {
    private String secret="135175@$moiN";
    public String extractUserName(String token)
    {
        return extractClamis(token,Claims::getSubject);
    }
    public Date extractExpiration(String token)
    {
        return extractClamis(token,Claims::getExpiration);
    }
    public <T> T extractClamis(String token, Function<Claims, T> claimResolver)
    {
        final Claims claims=extractAllClaim(token);
        return claimResolver.apply(claims);

    }
    public Claims extractAllClaim(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    public String generateToken(String username,String role)
    {
        Map<String ,Object> claims= new HashMap<>();
        claims.put("role",role);
        return createToken(claims,username);
    }
    private String createToken(Map<String,Object> claims,String subject)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String userName=extractUserName(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
