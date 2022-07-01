package com.hanghae99_team3.security.oauth2;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, OAuth2User oAuth2User) {
        switch (provider) {
            case "kakao":
                return new KakaoUserInfo(oAuth2User.getAttributes());
            case "naver":
                return new NaverUserInfo(oAuth2User.getAttributes());
            default:
                throw new OAuth2AuthenticationException(provider + " 로그인은 지원하지 않습니다.");
        }
    }
}
