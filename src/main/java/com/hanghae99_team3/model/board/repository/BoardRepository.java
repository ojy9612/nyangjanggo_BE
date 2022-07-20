package com.hanghae99_team3.model.board.repository;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> queryFirst5By(Pageable pageable);

    Page<Board> findAllByIdIn(List<Long> boardIdSet, Pageable pageable);

    Optional<Board> findByMainImageLink(String imageLink);

    Optional<Board> findByUserAndStatus(User user, String status);
}
