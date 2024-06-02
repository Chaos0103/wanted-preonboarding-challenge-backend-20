package com.wanted.challenge.api.controller.member;

import com.wanted.challenge.ControllerTestSupport;
import com.wanted.challenge.api.controller.member.request.MemberCreateRequest;
import com.wanted.challenge.api.controller.member.request.MemberLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.wanted.challenge.common.message.BindingMessage.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberApiControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/members";

    @DisplayName("신규 회원을 등록할 때 이메일은 필수값이다.")
    @Test
    void createMemberWithoutEmail() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
            .email(" ")
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_BLANK_MEMBER_EMAIL))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 회원을 등록할 때 비밀번호는 필수값이다.")
    @Test
    void createMemberWithoutPwd() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
            .email("wanted@gmail.com")
            .pwd(" ")
            .name("원티드")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_BLANK_MEMBER_PWD))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 회원을 등록할 때 이름은 필수값이다.")
    @Test
    void createMemberWithoutName() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name(" ")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_BLANK_MEMBER_NAME))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 회원을 등록한다.")
    @Test
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("로그인시 이메일은 필수값이다.")
    @Test
    void loginWithoutEmail() throws Exception {
        //given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email(" ")
            .pwd("wanted1234!")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL + "/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_BLANK_MEMBER_EMAIL))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인시 비밀번호는 필수값이다.")
    @Test
    void loginWithoutPwd() throws Exception {
        //given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("wanted@gmail.com")
            .pwd(" ")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL + "/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_BLANK_MEMBER_PWD))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원이 로그인을 한다.")
    @Test
    void login() throws Exception {
        //given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL + "/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
}