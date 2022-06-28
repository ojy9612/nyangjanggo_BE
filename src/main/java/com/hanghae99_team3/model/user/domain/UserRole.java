//Test
package com.hanghae99_team3.model.user.domain;

public enum UserRole {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "USER_TOKEN";
        public static final String ADMIN = "ADMIN_TOKEN";
    }
}
