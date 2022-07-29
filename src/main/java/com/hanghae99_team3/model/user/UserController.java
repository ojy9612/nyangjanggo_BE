package com.hanghae99_team3.model.user;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
import com.hanghae99_team3.model.fridge.dto.FridgeResponseDto;
import com.hanghae99_team3.model.user.domain.dto.NicknameDto;
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
    public void updateUser(@RequestPart UserReqDto userDto,
                           @RequestPart MultipartFile multipartFile,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUser(principalDetails.getUsername(), userDto, multipartFile, principalDetails);
    }

    @DeleteMapping("/api/user")
    public void deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails.getUsername(), principalDetails);
    }

    // 닉네임 중복 확인
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

}
