package com.sns.gobong.domain.dto.response.oauth;

import java.util.Map;
import java.util.Optional;

public class NaverResponse implements OAuth2Response {

    private final static String RESPONSE = "response";
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String EMAIL = "email";
    private final static String PROVIDER = "naver";
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get(RESPONSE);
    }

    @Override
    public String getProvider() {
        return PROVIDER;
    }

    @Override
    public String getProviderId() {
        return Optional.ofNullable(attribute.get(ID))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attribute.get(EMAIL))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    public String getName() {
        return Optional.ofNullable(attribute.get(NAME))
                .map(Object::toString)
                .orElse("");
    }
}
