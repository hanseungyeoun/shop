package com.example.shopbatch.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),
    DUPLICATED_USER_NAME("이미 등록된 회원 입니다."),
    INVALID_PASSWORD( "Pass word 가 일치 하지 않습니다."),
    INVALID_TOKEN("유효하지 않는 토큰입니다."),
    USER_NOTFOUND("가입하지 않은 아이디 입니다"),
    UNAUTHORIZED("로그인 되어있지 않습니다");



    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}