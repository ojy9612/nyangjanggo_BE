package com.hanghae99_team3.login.jwt;

import com.hanghae99_team3.login.jwt.dto.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/refresh")
    public TokenResponseDto refresh(HttpServletRequest request, HttpServletResponse response){

        return tokenService.refresh(request, response);
    }
}
