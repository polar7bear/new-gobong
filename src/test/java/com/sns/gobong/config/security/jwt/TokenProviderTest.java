package com.sns.gobong.config.security.jwt;

import com.sns.gobong.service.user.CustomUserDetailsService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TokenProviderTest {

    private static final Logger log = LoggerFactory.getLogger(TokenProviderTest.class);
    private TokenProvider tokenProvider;
    private CustomUserDetailsService userDetailsService;

    private final String issuer = "serverIssuer";

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);

        UserDetails userDetails = new User("sonny", "1234", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        tokenProvider = new TokenProvider(base64Secret, 3600, userDetailsService, issuer);
        tokenProvider.afterPropertiesSet();
    }

    @Test
    @DisplayName("Access Token 생성")
    void createAccessToken() {
        // given
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("sonny", "1234", Collections.singleton(simpleGrantedAuthority));

        // when
        String accessToken = tokenProvider.createAccessToken(authentication);
        Authentication authentication1 = tokenProvider.getAuthentication(accessToken);
        Collection<? extends GrantedAuthority> authorities = authentication1.getAuthorities();

        // then
        assertNotNull(accessToken);
        assertEquals("sonny", authentication1.getName());
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

    }

    @Test
    @DisplayName("토큰으로 사용자 정보 가져오기")
    void getAuthentication() {
        // given
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("sonny", "1234", Collections.singleton(simpleGrantedAuthority));

        // when
        String accessToken = tokenProvider.createAccessToken(authentication);
        Authentication result = tokenProvider.getAuthentication(accessToken);
        String role = result.getAuthorities().stream().findFirst().get().toString();

        // then
        assertNotNull(result);
        assertEquals("sonny", result.getName());
        assertEquals("ROLE_USER", role);
    }

    @Test
    void validationAccessToken() {
        // given
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("sonny", "1234", Collections.singleton(simpleGrantedAuthority));

        // when
        String accessToken = tokenProvider.createAccessToken(authentication);
        boolean result = tokenProvider.validationAccessToken(accessToken);

        // then
        assertTrue(result);
    }


    // TODO: 아래 테스트는 Refresh Token 서비스 로직 작성 후 테스트하자.
    /*@Test
    @DisplayName("Refresh Token으로 Access Token을 재생성 할 수 있어야한다.")
    void createRefreshAccessToken() {
        // given
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("sonny", "1234", Collections.singleton(simpleGrantedAuthority));
        String accessToken = tokenProvider.createAccessToken(authentication);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + accessToken);
        String token = tokenProvider.resolveToken(request);
        log.info("[TokenProviderTest createRefreshAccessToken] token: {}", token);

        // when
        String refreshAccessToken = tokenProvider.createRefreshAccessToken(authentication);
        // then
    }*/
}