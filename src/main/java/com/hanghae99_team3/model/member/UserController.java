package com.hanghae99_team3.model.member;

import com.hanghae99_team3.model.member.domain.UserRole;
import com.hanghae99_team3.model.member.dto.LoginMemberDto;
import com.hanghae99_team3.model.member.dto.SignupMemberDto;
import com.hanghae99_team3.model.member.dto.UserInfoDto;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class UserController {

    //Test
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }


//    @GetMapping("/login/oauth2/code/kakao")
//    public String kakaoLogin(@RequestParam String code) {
//        // authorizedCode: 카카오 서버로부터 받은 인가 코드
//        System.out.println("code = " + code);
//
//        return "redirect:/";
//    }

    @PostMapping("/join")
    public Long join(@RequestBody SignupMemberDto signupMemberDto) {
        return userService.join(signupMemberDto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginMemberDto loginMemberDto) {
        return userService.login(loginMemberDto);
    }

    @PostMapping("/member/memberInfo")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails);
        String username = principalDetails.getUsername();
        UserRole roles = principalDetails.getUser().getRole();
        return new UserInfoDto(username, roles);
    }

    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
        String result = "";

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        if(principal.getUser().getAuthProvider() == null) {
            result = result + "Form 로그인 : " + principal;
        }else{
            result = result + "OAuth2 로그인 : " + principal;
        }
        return result;
    }

}
