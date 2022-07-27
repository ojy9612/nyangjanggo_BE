package com.hanghae99_team3.login.jwt;

import com.hanghae99_team3.login.oauth2.userinfo.OAuth2UserInfo;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private String nickname;
    private String userImg;
    private String userDescription;
    private String email;
    private UserRole role;
    private AuthProvider authProvider;
    private Long userId;
//    private OAuth2UserInfo oAuth2UserInfo;
    private Map<String, Object> attributes;
    private String providerId;
    private boolean isNew;

    public PrincipalDetails() {

    }

    public PrincipalDetails(String nickname, String userImg, String userDescription, String email, UserRole role, AuthProvider authProvider, Long userId) {
        this.nickname = nickname;
        this.userImg = userImg;
        this.userDescription = userDescription;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
        this.userId = userId;
    }

    //UserDetails: Form 로그인
    public PrincipalDetails(User user) {
        this.nickname = user.getNickname();
        this.userImg = user.getUserImg();
        this.userDescription = user.getUserDescription();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.authProvider = user.getAuthProvider();
        this.userId = user.getId();
    }

    //OAuth2User : OAuth2 로그인
    public PrincipalDetails(User user, OAuth2UserInfo oAuth2UserInfo, boolean isNew) {
        this.nickname = user.getNickname();
        this.userImg = user.getUserImg();
        this.userDescription = user.getUserDescription();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.authProvider = user.getAuthProvider();
        this.userId = user.getId();
        this.attributes = oAuth2UserInfo.getAttributes();
        this.providerId = oAuth2UserInfo.getProviderId();
//        this.oAuth2UserInfo = oAuth2UserInfo;
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserImg() {
        return userImg;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public UserRole getRole() {
        return role;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    /**
     * UserDetails Methods
     * @return
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return email;
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
        return attributes;
    }


    @Override
    public String getName() {
        return providerId;
    }

}
