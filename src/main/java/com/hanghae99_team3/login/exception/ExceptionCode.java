package com.hanghae99_team3.login.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionCode {
    private final String code;
    private final String message;


    @Builder
    public ExceptionCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
