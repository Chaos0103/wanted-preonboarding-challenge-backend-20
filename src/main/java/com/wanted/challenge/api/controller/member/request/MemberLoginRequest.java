package com.wanted.challenge.api.controller.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    private String email;
    private String pwd;

    @Builder
    private MemberLoginRequest(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }
}
