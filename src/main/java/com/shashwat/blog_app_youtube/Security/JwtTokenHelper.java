package com.shashwat.blog_app_youtube.Security;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    // retrieve username from token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we'll need secret key
    private Claims getAllClaimsFromToken(String token) {
        //return Jwts.parser().setSigningKey(AppConstants.SECRET).parseClaimsJws(token).getBody();
        return Jwts.parserBuilder().setSigningKey(AppConstants.SECRET).build().parseClaimsJws(token).getBody();
    }

    //check if the token is expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating token -
        // 1. Define claims of the token , like Issuer , Expiration , Subject and the ID
        // 2. Sign the jwt using HS512 algo and secret key
        // 3. According to JWT Compact Serialization( https://tools.itef.org/html/draft-itef-jose-j)

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + AppConstants.JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, AppConstants.SECRET).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
