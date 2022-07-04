package com.hanghae99_team3.model.good;

import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoodController {

    private final GoodService goodService;

    @GetMapping("/api/board/{boardId}/good")
    public void createAndRemoveGood(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @PathVariable Long boardId){

        goodService.createAndRemoveGood(principalDetails,boardId);
    }

}
