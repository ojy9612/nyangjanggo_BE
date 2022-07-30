package com.hanghae99_team3.model.board.repository;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {


    Page<Board> findAllByIdInAndStatus(List<Long> boardIdList, Pageable pageable, String status);

    List<Board> findAllByIdIn(List<Long> boardIdSet);

    Optional<Board> findByUserAndStatus(User user, String status);

    List<Board> findFirst10ByStatus(Sort sort, String status);

    Page<Board> findAllByStatus(Pageable pageable, String status);
}
