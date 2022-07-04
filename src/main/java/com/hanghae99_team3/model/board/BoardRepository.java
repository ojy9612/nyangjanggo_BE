package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    Page<Board> findAll(Pageable pageable);

    Optional<Board> findByMainImage(String imageLink);

    Optional<Board> findByUserAndStatusStartsWith(User user, String status);
}
