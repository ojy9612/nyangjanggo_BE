package com.hanghae99_team3.login.oauth2.userinfo;

import lombok.NoArgsConstructor;

import java.util.Map;
@NoArgsConstructor
public class DummyUserInfo implements OAuth2UserInfo{
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getProviderId() {
        return "";
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getUserImg() {
        return null;
    }
}
