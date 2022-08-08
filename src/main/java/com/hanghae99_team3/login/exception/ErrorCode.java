package com.hanghae99_team3.login.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(401, "T001", "토큰이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(401, "TA002", "[Access-Token] Expired Token"),
    EXPIRED_REFRESH_TOKEN(401, "TR002", "[Refresh-Token] Expired Token"),
    NOT_EXPIRED_TOKEN_YET(401, "T003", "[Access-Token] Not Expired Token Yet"),
    ACCESS_SIGNATURE_NOT_MATCH(401, "TA004", "[Access-Token] 사용자 인증 실패"),
    REFRESH_SIGNATURE_NOT_MATCH(401, "TR004", "[Refresh-Token] 사용자 인증 실패"),
    REFRESH_TOKEN_NOT_FOUND(401, "T005", "Refresh-Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_MATCH(401, "T006", "Refresh-Token이 일치하지 않습니다."),
    ACCESS_TOKEN_NOT_EXIST(401, "TA007", "Access-Token 내놔"),
    REFRESH_TOKEN_NOT_EXIST(401, "TR007", "Refresh-Token 내놔"),

    ACCESS_DENIED(403, "F001", "접근 권한 없음");

    private final int status;
    private final String code;
    private final String message;
}
