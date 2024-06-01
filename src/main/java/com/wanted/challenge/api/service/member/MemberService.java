package com.wanted.challenge.api.service.member;

import com.wanted.challenge.api.service.member.request.MemberCreateServiceRequest;
import com.wanted.challenge.api.service.member.response.MemberCreateResponse;
import com.wanted.challenge.api.service.member.response.MemberTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    public MemberCreateResponse createMember(final MemberCreateServiceRequest request) {
        return null;
    }

    public MemberTokenResponse login(final String email, final String pwd) {
        return null;
    }
}
