package com.sns.gobong.config.security.jwt;

import com.sns.gobong.config.security.CustomUserDetails;
import com.sns.gobong.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidityTime;
    private final CustomUserDetailsService userDetailsService;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.token-validity-time}") long tokenValidityTime, CustomUserDetailsService userDetailsService) {
        this.secret = secret;
        this.tokenValidityTime = tokenValidityTime * 1000;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        /*인증 객체를 가져와 사용자 정보를가져온다.
         * 시간 설정*/
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        long now = new Date().getTime();
        Date expiration = new Date(now + this.tokenValidityTime);

        String encodedId = encodeId(userDetails.getId());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", encodedId)
                .claim("type", "Access")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiration)
                .compact();
    }

    public String createRefreshAccessToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        long validityTime = 60480000;
        Date expiration = new Date(new Date().getTime() + validityTime);
        String encodedId = encodeId(userDetails.getId());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", encodedId)
                .claim("type", "Refresh")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰으로 인증 객체 가져오기
    public Authentication getAuthentication(String token) {
        Claims claims = (Claims) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parse(token)
                .getBody();
        String email = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, token, new ArrayList<>()); // user role
    }

    // 토큰 유효성 검증
    public boolean validationAccessToken(String token) {
        try {
            Claims claims = (Claims) Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parse(token)
                    .getBody();
            String tokenType = claims.get("type", String.class);

            return !tokenType.equals("Refresh");

        } catch (SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException(e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException(e.getMessage());
        }
    }

    // 토큰 유효시간 가져오기
    public Date getAccessTokenExpire(String token) {
        Claims claims = (Claims) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parse(token)
                .getBody();
        return claims.getExpiration();
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }


    // 사용자 Id 인코딩
    public String encodeId(Long id) {
        byte[] bytes = ByteBuffer.allocate(4).putLong(id).array();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
