package com.hanghae99_team3.model.board.repository;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByIdIn(List<Long> boardIdSet, Pageable pageable);

    Optional<Board> findByMainImage(String imageLink);

    Optional<Board> findByUserAndStatusStartsWith(User user, String status);
}
