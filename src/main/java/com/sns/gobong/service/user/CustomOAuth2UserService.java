package com.sns.gobong.service.user;

import com.sns.gobong.domain.dto.response.oauth.*;
import com.sns.gobong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("[CustomOAuth2UserService loadUser] oAuth2User.getAttribute(): {}", oAuth2User.getAttributes());

        String registerationId = userRequest.getClientRegistration().getRegistrationId(); // 어느 Provider 인지

        OAuth2Response oAuth2Response = whichProvider(oAuth2User, registerationId);

        //String email = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        UserDto userDto = UserDto.builder()
                .email(oAuth2Response.getEmail())
                .nickname(oAuth2Response.getName())
                .role("ROLE_USER")
                .provider(oAuth2Response.getProvider())
                .build();

        return new CustomOAuth2User(userDto);
    }

    private OAuth2Response whichProvider(OAuth2User oAuth2User, String provider) {
        return switch (provider) {
            case "naver" -> new NaverResponse(oAuth2User.getAttributes());
            case "google" -> new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
            default -> null;
        };
    }
}
