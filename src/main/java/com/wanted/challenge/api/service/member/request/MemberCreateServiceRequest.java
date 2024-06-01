package com.wanted.challenge.api.service.member.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberCreateServiceRequest {

    private final String email;
    private final String pwd;
    private final String name;

    @Builder
    private MemberCreateServiceRequest(String email, String pwd, String name) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
    }
}
