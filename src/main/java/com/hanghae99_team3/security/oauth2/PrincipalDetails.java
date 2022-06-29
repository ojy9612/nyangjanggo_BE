package com.hanghae99_team3.security.oauth2;

import com.hanghae99_team3.model.user.domain.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User user;
    private OAuth2UserInfo oAuth2UserInfo;

    //UserDetails: Form 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth2User : OAuth2 로그인
    public PrincipalDetails(User user, OAuth2UserInfo oAuth2UserInfo) {
        this.user = user;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
//        return user.getPassword();
        return null;
    }


    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    /**
     * OAuth2User Methods
     * @return
     */

    @Override
    public Map<String, Object> getAttributes() {
//        return attributes;
        return oAuth2UserInfo.getAttributes();
    }


    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }


}
