package com.hanghae99_team3.login.oauth2;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.login.oauth2.userinfo.OAuth2UserInfo;
import com.hanghae99_team3.login.oauth2.userinfo.OAuth2UserInfoFactory;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PrincipalOauth2UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //provider 에 따라 OAuth2UserInfo 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User);


        String providerId = oAuth2UserInfo.getProviderId();
        String nickname = provider + "_" +providerId;  			// 사용자가 입력한 적은 없지만 만들어준다. Default username
        String userImg = oAuth2UserInfo.getUserImg() != null ? oAuth2UserInfo.getUserImg() : "";
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2UserInfo.getEmail();
        UserRole role = UserRole.USER;


        Optional<User> user = userRepository.findByEmail(email);


        //DB에 없는 사용자라면 회원처리
        if (user.isEmpty()) {
            User newUser = User.oauth2Register()
                    .nickname(nickname).password(password).email(email).userImg(userImg).role(role)
                    .provider(AuthProvider.valueOf(provider)).providerId(providerId)
                    .build();
            userRepository.save(newUser);

            return new PrincipalDetails(newUser, oAuth2UserInfo, true); //
        }

        return new PrincipalDetails(user.get(), oAuth2UserInfo, false); //
    }
}
