package com.sns.gobong.config.security.jwt;

import com.sns.gobong.service.user.CustomUserDetailsService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidityTime;
    private final CustomUserDetailsService userDetailsService;
    private Key key;

    private final String issuer;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.token-validity-time}") long tokenValidityTime,
                         CustomUserDetailsService userDetailsService,
                         @Value("${jwt.issuer}") String issuer) {
        this.secret = secret;
        this.tokenValidityTime = tokenValidityTime * 1000;
        this.userDetailsService = userDetailsService;
        this.issuer = issuer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        /*인증 객체를 가져와 사용자 정보를가져온다.
         * 시간 설정*/
        String authorities = authentication.getAuthorities().stream().findFirst().get().toString();
        long now = new Date().getTime();
        Date expiration = new Date(now + this.tokenValidityTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer(issuer)
                .claim("role", authorities)
                .claim("type", "Access")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiration)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        long validityTime = 60480000;
        Date expiration = new Date(new Date().getTime() + validityTime);
        String role = authentication.getAuthorities().stream().findFirst().get().toString();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer(issuer)
                .claim("role", role)
                .claim("type", "Refresh")
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰으로 인증 객체 가져오기
    public Authentication getAuthentication(String token) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(getRole(token)));
        return new UsernamePasswordAuthenticationToken(getUsername(token), null, authorities);
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public String getTokenType(String token) {
        return getClaims(token).get("type", String.class);
    }

    public String getIssuer(String token) {
        return getClaims(token).getIssuer();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검증
    public boolean validationAccessToken(String token) {
        try {
            String tokenType = commonValidation(token);
            if (tokenType == null) return false;

            return tokenType.equals("Access");

        } catch (SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException(e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException(e.getMessage());
        }
    }

    public boolean validationRefreshToken(String token) {
        try {
            String tokenType = commonValidation(token);
            if (tokenType == null) return false;

            return tokenType.equals("Refresh");

        } catch (SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException(e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException(e.getMessage());
        }
    }

    private String commonValidation(String token) {
        String tokenType = getTokenType(token);
        String targetIssuer = getIssuer(token);
        String targetSubject = getUsername(token);
        Authentication authentication = getAuthentication(token);

        if (!targetIssuer.equals(issuer)) return null;
        if (!targetSubject.equals(authentication.getName())) return null;
        return tokenType;
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
