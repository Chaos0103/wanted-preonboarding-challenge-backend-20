package com.wanted.challenge.api.service.member.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberTokenResponse {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private MemberTokenResponse(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
