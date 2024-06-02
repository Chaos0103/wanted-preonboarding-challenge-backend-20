package com.wanted.challenge.api.service.member;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.api.service.member.request.MemberCreateServiceRequest;
import com.wanted.challenge.api.service.member.response.MemberCreateResponse;
import com.wanted.challenge.api.service.member.response.MemberTokenResponse;
import com.wanted.challenge.common.exception.DuplicateException;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static com.wanted.challenge.common.message.ExceptionMessage.DUPLICATE_EMAIL;
import static com.wanted.challenge.common.message.ValidationMessage.*;
import static org.assertj.core.api.Assertions.*;

class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("입력 받은 이메일의 길이가 100자 초과하면 예외가 발생한다.")
    @Test
    void createMemberWithOutOfMaxLengthByEmail() {
        //given
        String email = "a".repeat(91) + "@gmail.com";
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email(email)
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.email: " + OUT_OF_LENGTH_MEMBER_EMAIL);
    }

    @DisplayName("입력 받은 이메일의 형식이 올바르지 않다면 예외가 발생한다.")
    @CsvSource({"@gmail.com", "wantedgmail.com", "wanted@.com", "wanted@gmailcom", "wanted@gmail."})
    @ParameterizedTest
    void createMemberWithNotMatchPatternByEmail(String email) {
        //given
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email(email)
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.email: " + NOT_MATCH_REGEX_MEMBER_EMAIL);
    }

    @DisplayName("입력 받은 비밀번호의 길이가 8자 미만이면 예외가 발생한다.")
    @Test
    void createMemberWithOutOfMinLengthByPwd() {
        //given
        String pwd = "want12!";
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd(pwd)
            .name("원티드")
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.pwd: " + OUT_OF_LENGTH_MEMBER_PWD);
    }

    @DisplayName("입력 받은 비밀번호의 길이가 20자 초과라면 예외가 발생한다.")
    @Test
    void createMemberWithOutOfMaxLengthByPwd() {
        //given
        String pwd = "wanted01234567890123!";
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd(pwd)
            .name("원티드")
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.pwd: " + OUT_OF_LENGTH_MEMBER_PWD);
    }

    @DisplayName("입력 받은 비밀번호에 영문, 숫자, 특수문자가 한 번 이상 포함되어 있지 않으면 예외가 발생한다.")
    @CsvSource({"wanted1234", "wanted!@@", "12341234!"})
    @ParameterizedTest
    void createMemberWithNotMatchPatternByPwd(String pwd) {
        //given
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd(pwd)
            .name("원티드")
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.pwd: " + NOT_MATCH_REGEX_MEMBER_PWD);
    }

    @DisplayName("입력 받은 이름의 길이가 20자 초과라면 예외가 발생한다.")
    @Test
    void createMemberWithOutOfMaxLengthByName() {
        //given
        String name = "김".repeat(21);
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name(name)
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.name: " + OUT_OF_LENGTH_MEMBER_NAME);
    }

    @DisplayName("입력 받은 이름에 한글 이외의 문자가 포함되어 있다면 예외가 발생한다.")
    @Test
    void createMemberWithNotMatchPatternByName() {
        //given
        String name = "Kim";
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name(name)
            .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createMember.request.name: " + NOT_MATCH_REGEX_MEMBER_NAME);
    }

    @DisplayName("입력 받은 이메일을 사용중인 회원 정보가 존재하면 예외가 발생한다.")
    @Test
    void createMemberDuplicatedEmail() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey, "wanted@gmail.com");

        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        //when
        assertThatThrownBy(() -> memberService.createMember(request))
            .isInstanceOf(DuplicateException.class)
            .hasMessage(DUPLICATE_EMAIL);

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @DisplayName("회원 정보를 입력 받아 신규 회원들 등록한다.")
    @Test
    void createMember() {
        //given
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();

        //when
        MemberCreateResponse response = memberService.createMember(request);

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("email", "wanted@gmail.com")
            .hasFieldOrPropertyWithValue("name", "원티드");

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1)
            .extracting("email", "name")
            .containsExactlyInAnyOrder(
                tuple("wanted@gmail.com", "원티드")
            );
    }

    private Member createMember(String memberKey, String email) {
        Member member = Member.builder()
            .isDeleted(false)
            .memberKey(memberKey)
            .email(email)
            .pwd("wanted1234!")
            .name("원티드")
            .build();
        return memberRepository.save(member);
    }
}