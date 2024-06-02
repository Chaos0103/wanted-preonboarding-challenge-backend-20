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
        Member member = createMember();

        //when
        boolean isExistEmail = memberRepository.existsByEmail(email);

        //then
        assertThat(isExistEmail).isEqualTo(expected);
    }

    @DisplayName("이메일로 회원을 조회한다.")
    @Test
    void findByEmail() {
        //given
        Member member = createMember();

        //when
        Optional<Member> findMember = memberRepository.findByEmail("wanted@gmail.com");

        //then
        assertThat(findMember).isPresent();
    }

    private Member createMember() {
        Member member = Member.builder()
            .isDeleted(false)
            .memberKey(UUID.randomUUID().toString())
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();
        return memberRepository.save(member);
    }
}
