package com.hanghae99_team3.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody TokenRequestDto tokenRequestDto){
        Map<String, String> accessToken = new HashMap<>();
        String newAccessToken = tokenService.refresh(tokenRequestDto);
        accessToken.put("Access-Token", newAccessToken);
        return accessToken;
    }
}
