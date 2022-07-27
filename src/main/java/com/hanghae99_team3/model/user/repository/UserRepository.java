package com.hanghae99_team3.model.user.repository;

import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
    Optional<User> findByUserImg(String UserImg);
    Boolean existsByEmail(String email);
}
