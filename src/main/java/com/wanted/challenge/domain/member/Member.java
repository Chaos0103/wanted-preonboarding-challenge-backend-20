package com.wanted.challenge.domain.member;

import com.wanted.challenge.domain.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, columnDefinition = "char(36)", length = 36)
    private String memberKey;

    @Column(unique = true, nullable = false, updatable = false, length = 100)
    private String email;

    @Column(nullable = false, columnDefinition = "char(60)", length = 60)
    private String pwd;

    @Column(nullable = false, length = 20)
    private String name;

    @Builder
    private Member(boolean isDeleted, String memberKey, String email, String pwd, String name) {
        super(isDeleted);
        this.memberKey = memberKey;
        this.email = email;
        this.pwd = pwd;
        this.name = name;
    }

    public static Member create(String email, String pwd, String name) {
        return Member.builder()
            .isDeleted(false)
            .memberKey(UUID.randomUUID().toString())
            .email(email)
            .pwd(pwd)
            .name(name)
            .build();
    }
}
