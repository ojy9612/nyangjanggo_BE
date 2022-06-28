package com.hanghae99_team3.security.oauth2;

import com.hanghae99_team3.model.member.domain.AuthProvider;
import com.hanghae99_team3.model.member.domain.User;
import com.hanghae99_team3.model.member.domain.UserRole;
import com.hanghae99_team3.model.member.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public PrincipalOauth2UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("google")){
//            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("naver")){
//            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("kakao")){	//추가
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }


        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId;  			// 사용자가 입력한 적은 없지만 만들어준다

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2UserInfo.getEmail();
        UserRole role = UserRole.USER;


        Optional<User> member = userRepository.findByEmail(email);
//        Member byUsername = (Member) member;

        //DB에 없는 사용자라면 회원처리
        if (member.isEmpty()) {
            User newUser = User.oauth2Register()
                    .username(username).password(password).email(email).role(role)
                    .provider(AuthProvider.kakao).providerId(providerId)
                    .build();
            userRepository.save(newUser);
            return new PrincipalDetails(newUser, oAuth2UserInfo);
        }

        return new PrincipalDetails(member.get(), oAuth2UserInfo);
    }
}
