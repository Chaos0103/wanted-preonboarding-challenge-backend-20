package com.wanted.challenge.api.service.member.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import static com.wanted.challenge.common.message.ValidationMessage.*;

@Getter
public class MemberCreateServiceRequest {

    @Size(max = 100, message = OUT_OF_LENGTH_MEMBER_EMAIL)
    @Pattern(regexp = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]+$", message = NOT_MATCH_REGEX_MEMBER_EMAIL)
    private final String email;

    @Size(min = 8, max = 20, message = OUT_OF_LENGTH_MEMBER_PWD)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{0,100}$", message = NOT_MATCH_REGEX_MEMBER_PWD)
    private final String pwd;

    @Size(max = 20, message = OUT_OF_LENGTH_MEMBER_NAME)
    @Pattern(regexp = "^[가-핳]*$", message = NOT_MATCH_REGEX_MEMBER_NAME)
    private final String name;

    @Builder
    private MemberCreateServiceRequest(String email, String pwd, String name) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
    }
}
