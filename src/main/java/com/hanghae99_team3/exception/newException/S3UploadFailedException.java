package com.hanghae99_team3.exception.newException;

public class S3UploadFailedException extends RuntimeException {

    public S3UploadFailedException(String message) {
        super(message);
    }
}
