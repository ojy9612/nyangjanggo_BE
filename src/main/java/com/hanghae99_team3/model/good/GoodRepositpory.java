package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface GoodRepositpory extends JpaRepository<Good, Long> {

    Optional<Good> findByBoardAndUser(Board board, User user);

}
