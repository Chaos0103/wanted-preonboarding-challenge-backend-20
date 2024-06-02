package com.wanted.challenge.api.controller.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.wanted.challenge.common.message.BindingMessage.NOT_BLANK_MEMBER_EMAIL;
import static com.wanted.challenge.common.message.BindingMessage.NOT_BLANK_MEMBER_PWD;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank(message = NOT_BLANK_MEMBER_EMAIL)
    private String email;

    @NotBlank(message = NOT_BLANK_MEMBER_PWD)
    private String pwd;

    @Builder
    private MemberLoginRequest(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }
}
