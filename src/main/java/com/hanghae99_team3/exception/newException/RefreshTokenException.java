package com.hanghae99_team3.exception.newException;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException() {
        super("RefreshToken 만료 / 다시 로그인 필요");
    }
    public RefreshTokenException(String message) {
        super(message);
    }
}
