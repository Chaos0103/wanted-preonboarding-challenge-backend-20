package com.wanted.challenge.api.controller.member;

import com.wanted.challenge.api.ApiResponse;
import com.wanted.challenge.api.controller.member.request.MemberCreateRequest;
import com.wanted.challenge.api.controller.member.request.MemberLoginRequest;
import com.wanted.challenge.api.service.member.MemberService;
import com.wanted.challenge.api.service.member.response.MemberCreateResponse;
import com.wanted.challenge.api.service.member.response.MemberTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 신규 등록 API
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberCreateResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberService.createMember(request.toServiceRequest());

        return ApiResponse.created(response);
    }

    /**
     * 회원 계정 로그인 API
     */
    @PostMapping("/login")
    public ApiResponse<MemberTokenResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        MemberTokenResponse response = memberService.login(request.getEmail().strip(), request.getPwd().strip());

        return ApiResponse.ok(response);
    }
}
