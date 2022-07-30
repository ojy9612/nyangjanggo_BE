package com.hanghae99_team3.model.user;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
import com.hanghae99_team3.model.fridge.dto.FridgeResponseDto;
import com.hanghae99_team3.model.user.domain.dto.UserReqDto;
import com.hanghae99_team3.model.user.domain.dto.UserResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * User 정보 조회 API
     * @param principalDetails : 인증된 유저 정보
     * @return UserResDto : 닉네임, 유저사진, 자기소개글
     */
    @GetMapping("/api/user")
    public UserResDto getUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new UserResDto(
                principalDetails.getNickname(),
                principalDetails.getUserImg(),
                principalDetails.getUserDescription()
        );
    }


    /**
     * User 정보 수정 API
     * @param userDto : 변경할 nickname, userDescription
     * @param multipartFile : 변경할 유저 사진
     * @param principalDetails : 인증된 유저 정보
     */
    @PutMapping("/api/user")
    public void updateUser(@RequestPart UserReqDto userDto,
                           @RequestPart MultipartFile multipartFile,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUser(principalDetails.getUsername(), userDto, multipartFile, principalDetails);
    }


    /**
     * User 탈퇴 API
     * @param principalDetails 인증된 유저 정보
     */
    @DeleteMapping("/api/user")
    public void deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails.getUsername(), principalDetails);
    }


    /**
     * User Nickname 중복 확인 API
     * @param nickname : 중복 확인할 nickname
     * @return 사용할 수 있다면 true, 중복된 닉네임이면 false
     */
    @GetMapping("/api/user/checkNickname")
    public ResponseEntity<Map<String, String>> checkNickname(@RequestParam String nickname) {
        Map<String, String> response = new HashMap<>();
        if (userService.checkNicknameDup(nickname)) {
            response.put("check", "true");
        } else {
            response.put("check", "false");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/api/user/fridge")
    public List<FridgeResponseDto> getFridge(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return userService.getFridge(principalDetails).stream()
                .map(FridgeResponseDto::new).collect(Collectors.toList());
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

    @GetMapping("/test/user/userInfo")
    public String testUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails.getUserImg();
    }

}
