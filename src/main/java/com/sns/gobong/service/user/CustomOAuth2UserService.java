package com.sns.gobong.service.user;

import com.sns.gobong.domain.dto.response.oauth.*;
import com.sns.gobong.domain.dto.response.user.UserSignInResponseDto;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.exception.user.UserAlreadyExistsException;
import com.sns.gobong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        String email = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        UserDto userDto = UserDto.builder()
                .email(email)
                .nickname(oAuth2Response.getName())
                .role("ROLE_USER")
                .build();

        return new CustomOAuth2User(userDto);
    }

    private User oauthSignIn(UserDto dto) {
        Optional<User> finded = userRepository.findByEmail(dto.getEmail());
        if (finded.isPresent()) {
            // TODO: 유니크한 값인 본명이나 닉네임으로 찾아서 가입되어있다면 이미 가입한 유저라고 응답하고, 가입되어 있지않다면 현재로직에서 save 하거나 OAuth 전용 회원가입으로 redirect?
        }
        return null;
    }



    private OAuth2Response whichProvider(OAuth2User oAuth2User, String provider) {
        if (provider.equals("naver")) {
            return new NaverResponse(oAuth2User.getAttributes());
        } else if (provider.equals("google")) {
            return new GoogleResponse(oAuth2User.getAttributes());
        }
        return null;
    }
}
