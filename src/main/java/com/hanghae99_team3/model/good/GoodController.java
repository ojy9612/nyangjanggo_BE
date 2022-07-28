package com.hanghae99_team3.model.good;

import com.hanghae99_team3.config.redis.CacheKey;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoodController {

    private final GoodService goodService;

    @CacheEvict(value = CacheKey.BOARD, key = "#boardId", cacheManager = "cacheManager")
    @GetMapping("/api/board/{boardId}/good")
    public void createAndRemoveGood(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @PathVariable Long boardId){

        goodService.createAndRemoveGood(principalDetails,boardId);
    }



}
