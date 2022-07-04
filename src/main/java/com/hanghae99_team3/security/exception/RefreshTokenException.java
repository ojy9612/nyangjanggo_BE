package com.hanghae99_team3.security.exception;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException() {
        super("RefreshToken 만료 / 다시 로그인 필요");
    }
    public RefreshTokenException(String message) {
        super(message);
    }
}
