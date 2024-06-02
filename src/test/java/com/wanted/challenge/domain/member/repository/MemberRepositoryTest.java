package com.wanted.challenge.domain.member.repository;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일을 사용중인 회원의 존재 여부를 조회한다.")
    @CsvSource({"wanted@gmail.com,true", "wanted@naver.com,false"})
    @ParameterizedTest
    void existsByEmail(String email, boolean expected) {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        //when
        boolean isExistEmail = memberRepository.existsByEmail(email);

        //then
        assertThat(isExistEmail).isEqualTo(expected);
    }

    @DisplayName("이메일로 회원을 조회한다.")
    @Test
    void findByEmail() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        //when
        Optional<Member> findMember = memberRepository.findByEmail("wanted@gmail.com");

        //then
        assertThat(findMember).isPresent();
    }

    @DisplayName("회원 고유키로 회원을 조회한다.")
    @Test
    void findByMemberKey() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        //when
        Optional<Member> findMember = memberRepository.findByMemberKey(memberKey);

        //then
        assertThat(findMember).isPresent();
    }

    private Member createMember(String memberKey) {
        Member member = Member.builder()
            .isDeleted(false)
            .memberKey(memberKey)
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();
        return memberRepository.save(member);
    }
}
