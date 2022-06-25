package com.hanghae99_team3.model.member.repository;

import com.hanghae99_team3.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String email);
}
