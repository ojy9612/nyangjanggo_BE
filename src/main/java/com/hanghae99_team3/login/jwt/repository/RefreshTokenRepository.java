package com.hanghae99_team3.login.jwt.repository;

import com.hanghae99_team3.login.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserPk(String userPk);

    void deleteByUserPk(String userPk);
}
