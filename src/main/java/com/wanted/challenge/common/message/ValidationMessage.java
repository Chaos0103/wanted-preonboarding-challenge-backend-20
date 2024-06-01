package com.wanted.challenge.common.message;

public abstract class ValidationMessage {

    public static final String OUT_OF_LENGTH_MEMBER_EMAIL = "이메일의 길이는 최대 100자리 입니다.";
    public static final String NOT_MATCH_REGEX_MEMBER_EMAIL = "이메일을 올바르게 입력해주세요.";
    public static final String OUT_OF_LENGTH_MEMBER_PWD = "비밀번호의 길이는 최소 8자리, 최대 20자리 입니다.";
    public static final String NOT_MATCH_REGEX_MEMBER_PWD = "비밀번호를 올바르게 입력해주세요.";
    public static final String OUT_OF_LENGTH_MEMBER_NAME = "이름의 길이는 최대 20자리 입니다.";
    public static final String NOT_MATCH_REGEX_MEMBER_NAME = "이름을 올바르게 입력해주세요.";
}
