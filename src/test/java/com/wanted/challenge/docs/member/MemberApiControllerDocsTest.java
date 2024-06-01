package com.wanted.challenge.docs.member;

import com.wanted.challenge.api.controller.member.MemberApiController;
import com.wanted.challenge.api.controller.member.request.MemberCreateRequest;
import com.wanted.challenge.api.controller.member.request.MemberLoginRequest;
import com.wanted.challenge.api.service.member.MemberService;
import com.wanted.challenge.api.service.member.response.MemberCreateResponse;
import com.wanted.challenge.api.service.member.response.MemberTokenResponse;
import com.wanted.challenge.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/members";
    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberApiController(memberService);
    }

    @DisplayName("회원 신규 등록 API")
    @Test
    void createProduct() throws Exception {
        MemberCreateRequest request = MemberCreateRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        MemberCreateResponse response = MemberCreateResponse.builder()
            .email("wanted@gmail.com")
            .name("원티드")
            .createdDateTime(LocalDateTime.of(2024, 1, 1, 13, 30))
            .build();

        given(memberService.createMember(any()))
            .willReturn(response);

        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-member",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("신규 등록할 계정 이메일"),
                    fieldWithPath("pwd").type(JsonFieldType.STRING)
                        .description("신규 등록할 계정 비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("신규 등록할 계정 회원명")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                        .description("신규 등록된 계정 이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("신규 등록된 회원명"),
                    fieldWithPath("data.createdDateTime").type(JsonFieldType.ARRAY)
                        .description("신규 계정 가입 일시")
                )
            ));
    }

    @DisplayName("회원 로그인 API")
    @Test
    void login() throws Exception {
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .build();

        MemberTokenResponse response = MemberTokenResponse.builder()
            .grantType("Bearer")
            .accessToken("access.token.info")
            .refreshToken("refresh.token.info")
            .build();

        given(memberService.login(anyString(), anyString()))
            .willReturn(response);

        mockMvc.perform(
                post(BASE_URL + "/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("login-member",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("계정 이메일"),
                    fieldWithPath("pwd").type(JsonFieldType.STRING)
                        .description("계정 비밀번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.grantType").type(JsonFieldType.STRING)
                        .description("발급된 토큰 유형"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                        .description("발급된 접근 토큰"),
                    fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                        .description("발급된 갱신 토큰")
                )
            ));
    }
}
