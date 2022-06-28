package com.hanghae99_team3.model.member;


import com.hanghae99_team3.model.member.domain.User;
import com.hanghae99_team3.model.member.dto.LoginMemberDto;
import com.hanghae99_team3.model.member.dto.SignupMemberDto;
import com.hanghae99_team3.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    // Test용
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

//    private final PasswordEncoder passwordEncoder;


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
}
