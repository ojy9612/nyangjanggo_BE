package com.hanghae99_team3.model.board.repository;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface BoardRepository extends JpaRepository<Board, Long> {


    Page<Board> findAllByIdIn(List<Long> boardIdList, Pageable pageable);
    List<Board> findAllByIdIn(List<Long> boardIdSet);

    @Query("select b from Board b order by :columName")
    Page<Board> getBoardByUserGood(@Param(value = "columName") String columName, Pageable pageable);

    Optional<Board> findByUserAndStatus(User user, String status);

    List<Board> findFirst10By(Sort sort);

    Page<Board> findAll(Pageable pageable);
}
