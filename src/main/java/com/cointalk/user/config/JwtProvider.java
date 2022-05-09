package com.cointalk.user.config;

import com.cointalk.user.entity.User;
import com.cointalk.user.model.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${secret}")
    public String secret;

    @Value("${refreshExpire}")
    public String refreshExpireString;

    @Value("${accessExpire}")
    public String accessExpireString;

    private String generateToken(User user, long interval) {
        Date expiration = new Date(System.currentTimeMillis() + interval);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("nickName", user.getNickName())
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiration)
                .compact();
    }

    public String generateRefreshToken(User user) {
        long refreshExpire = Long.parseLong(refreshExpireString.trim());
        return generateToken(user, refreshExpire);
    }

    public String generateAccessToken(User user) {
        long accessExpire = Long.parseLong(accessExpireString.trim());
        return generateToken(user, accessExpire);
    }

    public TokenInfo getTokenInfo(String accessToken) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        Claims tokenClaims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        return TokenInfo.create(tokenClaims);
    }
}
