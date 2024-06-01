package com.wanted.challenge.api.service.member;

import com.wanted.challenge.api.service.member.request.MemberCreateServiceRequest;
import com.wanted.challenge.api.service.member.response.MemberCreateResponse;
import com.wanted.challenge.api.service.member.response.MemberTokenResponse;
import com.wanted.challenge.common.exception.DuplicateException;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.wanted.challenge.common.message.ExceptionMessage.DUPLICATE_EMAIL;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberCreateResponse createMember(@Valid final MemberCreateServiceRequest request) {
        boolean isExistEmail = memberRepository.existsByEmail(request.getEmail());
        if (isExistEmail) {
            throw new DuplicateException(DUPLICATE_EMAIL);
        }

        final String encodedPwd = passwordEncoder.encode(request.getEmail());

        final Member member = Member.create(request.getEmail(), encodedPwd, request.getName());
        final Member savedMember = memberRepository.save(member);

        return MemberCreateResponse.of(savedMember);
    }

    public MemberTokenResponse login(final String email, final String pwd) {
        return null;
    }
}
