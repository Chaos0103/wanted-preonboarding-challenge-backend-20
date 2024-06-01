package com.wanted.challenge.api.controller.member.request;

import com.wanted.challenge.api.service.member.request.MemberCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    private String email;
    private String pwd;
    private String name;

    @Builder
    private MemberCreateRequest(String email, String pwd, String name) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
    }

    public MemberCreateServiceRequest toServiceRequest() {
        return null;
    }
}
