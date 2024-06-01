package com.wanted.challenge.api.service.member.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberCreateResponse {

    private final String email;
    private final String name;
    private final LocalDateTime createdDateTime;

    @Builder
    private MemberCreateResponse(String email, String name, LocalDateTime createdDateTime) {
        this.email = email;
        this.name = name;
        this.createdDateTime = createdDateTime;
    }
}
