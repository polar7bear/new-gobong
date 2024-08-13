package com.sns.gobong.domain.dto.response.oauth;

import java.util.Map;
import java.util.Optional;

public class KakaoResponse implements OAuth2Response {

    private final static String ID = "id";
    private final static String NICKNAME = "nickname";
    private final static String EMAIL = "email";
    private final static String PROVIDER = "kakao";
    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return PROVIDER;
    }

    @Override
    public String getProviderId() {
        return Optional.ofNullable(attributes.get(ID))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attributes.get("kakao_account"))
                .map(props -> ((Map<?, ?>) props).get(EMAIL))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    public String getName() {
        return Optional.ofNullable(attributes.get("properties"))
                .map(props -> ((Map<?, ?>) props).get(NICKNAME))
                .map(Object::toString)
                .orElse("기본닉네임");
    }
}

