package com.hanghae99_team3.model.user;


import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.dto.LoginMemberDto;
import com.hanghae99_team3.model.user.dto.SignupMemberDto;
import com.hanghae99_team3.model.user.dto.UserReqDto;
import com.hanghae99_team3.security.jwt.JwtTokenProvider;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    // Test용
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

//    private final PasswordEncoder passwordEncoder;


    public String update(UserReqDto userReqDto, PrincipalDetails principalDetails) {
        User user = userRepository.findByEmail(principalDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다."));
         // 빌더로
        return "작업중";
    }

    public Long join(SignupMemberDto memberDto) {
        String username = memberDto.getUsername();
        Optional<User> found = userRepository.findByEmail(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        User user = new User(username);
        return userRepository.save(user).getId();
    }

    public Map<String, String> login(LoginMemberDto memberDto) {
        Map<String, String> token = new HashMap<>();

        User user = userRepository.findByEmail(memberDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 username 입니다."));

        token.put("Access-Token", jwtTokenProvider.createToken(user.getUsername(), user.getRole()));
        return token;
    }



    public String deleteUser(PrincipalDetails principalDetails) {
        return "작업필요";
    }
}
