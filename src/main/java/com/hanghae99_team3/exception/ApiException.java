package com.hanghae99_team3.exception;

import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.exception.newException.S3UploadFailedException;
import com.hanghae99_team3.login.exception.ErrorCode;
import com.hanghae99_team3.login.exception.ExceptionCode;
import com.hanghae99_team3.login.exception.RefreshTokenException;
import com.hanghae99_team3.login.exception.NotExpiredTokenYetException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiException extends RuntimeException {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("IllegalArgumentException", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(S3UploadFailedException.class)
    public ResponseEntity<Map<String, String>> handleS3UploadFailedException(S3UploadFailedException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("S3UploadFailedException", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IdDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleIdDuplicateException(IdDuplicateException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("IdDuplicateException", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }

    /**
     * SseEmitter Exception Handler
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleSseRuntimeException(RuntimeException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("SSE 연결 오류!", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }


    /**
     * Refresh Token Exception Handler
     */
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ExceptionCode> handleRefreshTokenException(RefreshTokenException e) {
        ErrorCode errorCode;
        log.error(e.getMessage());
        switch (e.getMessage()) {

            case "RefreshToken을 찾을 수 없습니다.":
                errorCode = ErrorCode.REFRESH_TOKEN_NOT_FOUND;
                return ResponseEntity.status(errorCode.getStatus())
                        .body(new ExceptionCode(errorCode));
            case "RefreshToken이 일치하지 않습니다.":
                errorCode = ErrorCode.REFRESH_TOKEN_NOT_MATCH;
                return ResponseEntity.status(errorCode.getStatus())
                        .body(new ExceptionCode(errorCode));
            case "RefreshToken이 없습니다.":
                errorCode = ErrorCode.REFRESH_TOKEN_NOT_EXIST;
                return ResponseEntity.status(errorCode.getStatus())
                        .body(new ExceptionCode(errorCode));
            default:
                errorCode = ErrorCode.INVALID_TOKEN;
                return ResponseEntity.status(errorCode.getStatus())
                        .body(new ExceptionCode(errorCode));
        }
    }

    @ExceptionHandler(NotExpiredTokenYetException.class)
    public ResponseEntity<ExceptionCode> handleTokenExpiredYetException(NotExpiredTokenYetException e) {
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.NOT_EXPIRED_TOKEN_YET;
        return ResponseEntity.status(errorCode.getStatus()).body(new ExceptionCode(errorCode));
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionCode> handleExpiredRefreshTokenException(ExpiredJwtException e) {
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.EXPIRED_REFRESH_TOKEN;
        return ResponseEntity.status(errorCode.getStatus()).body(new ExceptionCode(errorCode));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionCode> handleRefreshSignException(SignatureException e) {
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.REFRESH_SIGNATURE_NOT_MATCH;
        return ResponseEntity.status(errorCode.getStatus()).body(new ExceptionCode(errorCode));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionCode> handleRefreshTokenException(JwtException e) {
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        return ResponseEntity.status(errorCode.getStatus()).body(new ExceptionCode(errorCode));
    }
}
