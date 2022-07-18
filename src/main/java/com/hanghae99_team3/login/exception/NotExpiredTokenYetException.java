package com.hanghae99_team3.login.exception;


public class NotExpiredTokenYetException extends RuntimeException {

    public NotExpiredTokenYetException() {
        super("Access-Token 만료기간이 남아있습니다.");
    }

}
