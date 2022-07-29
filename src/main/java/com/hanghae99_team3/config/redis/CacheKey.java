package com.hanghae99_team3.config.redis;


public class CacheKey {
    private CacheKey() {
    }

    public static final String USER = "User";
    public static final String BOARD = "Board";
    public static final int USER_EXPIRE_SEC = 30 * 60;
    public static final int DEFAULT_EXPIRE_SEC = 60;
}