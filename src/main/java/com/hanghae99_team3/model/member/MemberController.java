package com.hanghae99_team3.model.member;

import com.hanghae99_team3.model.member.domain.Member;
import com.hanghae99_team3.model.member.domain.UserRole;
import com.hanghae99_team3.model.member.dto.LoginMemberDto;
import com.hanghae99_team3.model.member.dto.SignupMemberDto;
import com.hanghae99_team3.model.member.dto.UserInfoDto;
import com.hanghae99_team3.model.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class MemberController {

    //Test
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public Long join(@RequestBody SignupMemberDto signupMemberDto) {
        return memberService.join(signupMemberDto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginMemberDto loginMemberDto) {
        return memberService.login(loginMemberDto);
    }

    @PostMapping("/member/memberInfo")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal Member userDetails) {
        String username = userDetails.getUsername();
        UserRole roles = userDetails.getRoles();
        return new UserInfoDto(username, roles);
    }
}
