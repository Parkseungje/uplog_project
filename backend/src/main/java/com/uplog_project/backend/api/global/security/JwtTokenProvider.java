package com.uplog_project.backend.api.global.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt-secret-key}")
    private String SECRET_KEY; // 환경변수로 빼는게 좋음
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    // 토큰 생성
    public String createToken(String email, String name, String provider, String providerUserId) {
        return Jwts.builder()
                .setSubject(email) //고유 식별자
                .claim("email", email) // 명시적으로 claim에 email도 추가
                .claim("name", name)   // 유저 이름 추가
                .claim("provider", provider)
                .claim("providerUserId", providerUserId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                .getBody().getSubject();
    }

    // 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
