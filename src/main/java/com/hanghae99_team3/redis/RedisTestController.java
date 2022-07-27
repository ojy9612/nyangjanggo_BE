package com.hanghae99_team3.redis;

import com.hanghae99_team3.config.CacheKey;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.dto.NicknameDto;
import com.hanghae99_team3.model.user.domain.dto.UserResDto;
import com.hanghae99_team3.model.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedisTestController {

    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisTestController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PostMapping("/test/redisTest")
    public ResponseEntity<?> addRedisKey() {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("yellow", "banana");
        vop.set("red", "apple");
        vop.set("green", "watermelon");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @Cacheable(value = CacheKey.USER, key = "#email", unless = "#result == null", cacheManager = "cacheManager")
    @GetMapping("/test/redisTest/user/{email}")
    public User findUser(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user;
//        UserResDto userDto = new UserResDto(user.getNickname(), user.getUserImg(), user.getUserDescription());
//        return userDto;
    }

//    @CachePut(value = CacheKey.USER, key = "#userId", cacheManager = "cacheManager")
//    @PutMapping("/test/redisTest/user/{userId}")
//    public void updateUser(@PathVariable long userId, @RequestBody NicknameDto nicknameDto) {
//        userService.testUpdateUser(nicknameDto, userId);
//    }

}
