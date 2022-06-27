package com.hanghae99_team3.exception;

import com.hanghae99_team3.exception.newException.IdDuplicateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiException extends RuntimeException{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("IllegalArgumentException",e.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IdDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleIdDuplicateException(IdDuplicateException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("IdDuplicateException",e.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

}
