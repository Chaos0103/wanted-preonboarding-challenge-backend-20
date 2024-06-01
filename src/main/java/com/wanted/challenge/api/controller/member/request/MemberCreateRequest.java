package com.wanted.challenge.api.controller.member.request;

import com.wanted.challenge.api.service.member.request.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.wanted.challenge.common.message.BindingMessage.*;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank(message = NOT_BLANK_MEMBER_EMAIL)
    private String email;

    @NotBlank(message = NOT_BLANK_MEMBER_PWD)
    private String pwd;

    @NotBlank(message = NOT_BLANK_MEMBER_NAME)
    private String name;

    @Builder
    private MemberCreateRequest(String email, String pwd, String name) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
    }

    public MemberCreateServiceRequest toServiceRequest() {
        return MemberCreateServiceRequest.builder()
            .email(email.strip())
            .pwd(pwd.strip())
            .name(name.strip())
            .build();
    }
}
