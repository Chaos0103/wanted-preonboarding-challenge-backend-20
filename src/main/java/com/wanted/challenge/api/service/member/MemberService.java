package com.wanted.challenge.api.service.member;

import com.wanted.challenge.api.service.member.request.MemberCreateServiceRequest;
import com.wanted.challenge.api.service.member.response.MemberCreateResponse;
import com.wanted.challenge.api.service.member.response.MemberTokenResponse;
import com.wanted.challenge.common.exception.DuplicateException;
import com.wanted.challenge.common.security.JwtTokenProvider;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;

import static com.wanted.challenge.common.message.ExceptionMessage.DUPLICATE_EMAIL;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberCreateResponse createMember(@Valid final MemberCreateServiceRequest request) {
        boolean isExistEmail = memberRepository.existsByEmail(request.getEmail());
        if (isExistEmail) {
            throw new DuplicateException(DUPLICATE_EMAIL);
        }

        final String encodedPwd = passwordEncoder.encode(request.getPwd());

        final Member member = Member.create(request.getEmail(), encodedPwd, request.getName());
        final Member savedMember = memberRepository.save(member);

        return MemberCreateResponse.of(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberTokenResponse login(final String email, final String pwd) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, pwd);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(NoSuchElementException::new);

        return createMemberUserDetails(member);
    }

    private UserDetails createMemberUserDetails(final Member member) {
        return User.builder()
            .username(member.getMemberKey())
            .password(member.getPwd())
            .roles("USER")
            .build();
    }
}
