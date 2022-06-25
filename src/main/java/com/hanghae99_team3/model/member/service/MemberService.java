package com.hanghae99_team3.model.member.service;


import com.hanghae99_team3.model.member.domain.Member;
import com.hanghae99_team3.model.member.dto.LoginMemberDto;
import com.hanghae99_team3.model.member.dto.SignupMemberDto;
import com.hanghae99_team3.model.member.repository.UserRepository;
import com.hanghae99_team3.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    public Long join(SignupMemberDto memberDto) {
        String username = memberDto.getUsername();
        Optional<Member> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        Member member = new Member(username);
        return userRepository.save(member).getId();
    }

    public Map<String, String> login(LoginMemberDto memberDto) {
        Map<String, String> token = new HashMap<>();

        Member member = userRepository.findByUsername(memberDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 username 입니다."));

        token.put("Access-Token", jwtTokenProvider.createToken(member.getUsername(), member.getRoles()));
        return token;
    }
}
