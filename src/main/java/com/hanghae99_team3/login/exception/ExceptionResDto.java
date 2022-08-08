package com.hanghae99_team3.login.exception;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResDto {
    private final String code;
    private final String message;


    @Builder
    public ExceptionResDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
