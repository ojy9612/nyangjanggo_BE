package com.hanghae99_team3.login.jwt;

import com.hanghae99_team3.config.CacheKey;
import com.hanghae99_team3.login.oauth2.userinfo.DummyUserInfo;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@EnableCaching
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
//    @Cacheable(value = CacheKey.USER, key = "#email", unless = "#result == null", cacheManager = "cacheManager")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUser(email);
        return new PrincipalDetails(user,new DummyUserInfo(), false);
    }

    @Cacheable(value = CacheKey.USER, key = "#email", unless = "#result == null", cacheManager = "cacheManager")
    public User findUser(String email) {
        System.out.println("UserDetailsService에서 User찾기");
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

}
