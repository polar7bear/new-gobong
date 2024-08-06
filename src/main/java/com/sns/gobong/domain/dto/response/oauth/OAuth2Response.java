package com.sns.gobong.domain.dto.response.oauth;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
