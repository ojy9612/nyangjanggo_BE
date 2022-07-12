package com.hanghae99_team3.model.user;

import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.model.user.dto.*;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    //Test
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/api/user")
    public UserResDto getUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new UserResDto(
                principalDetails.getNickname(),
                principalDetails.getUserImg(),
                principalDetails.getUserDescription()
        );
    }

    @PutMapping("/api/user")
    public void updateUser(@ModelAttribute UserReqDto userDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUser(userDto, principalDetails);
    }

    @DeleteMapping("/api/user")
    public void deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails);
    }

    @PostMapping("/api/user/fridge")
    public void createFridge(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @RequestPart List<FridgeRequestDto> fridgeRequestDtoList){
        userService.createFridge(principalDetails,fridgeRequestDtoList);
    }

    @PutMapping("/api/user/fridge")
    public void updateFridge(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @RequestPart List<FridgeRequestDto> fridgeRequestDtoList){
        userService.updateFridge(principalDetails,fridgeRequestDtoList);
    }


//    @RequestMapping("/auth")
//    public Authentication auth() {
//        return SecurityContextHolder.getContext()
//                .getAuthentication();
//    }

//    @PostMapping("/join")
//    public Long join(@RequestBody SignupMemberDto signupMemberDto) {
//        return userService.join(signupMemberDto);
//    }

//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody LoginMemberDto loginMemberDto) {
//        return userService.login(loginMemberDto);
//    }

    @PostMapping("/member/memberInfo")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails);
        String username = principalDetails.getUsername();
        UserRole roles = principalDetails.getRole();
        return new UserInfoDto(username, roles);
    }

//    @PostMapping("/member/memberInfo1")
//    public UserInfoDto getUserInfo1(@AuthenticationPrincipal UserDetails userDetails) {
//        System.out.println("userDetails = " + userDetails);
//        String username = userDetails.getUsername();
//        UserRole roles = UserRole.USER;
//        return new UserInfoDto(username, roles);
//    }

    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication){
        String result = "";

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        if(principal.getAuthProvider() == null) {
            result = result + "Form 로그인 : " + principal;
        }else{
            result = result + "OAuth2 로그인 : " + principal;
        }
        return result;
    }

}
