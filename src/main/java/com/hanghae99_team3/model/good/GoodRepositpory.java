package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface GoodRepositpory extends JpaRepository<Good, Long> {
    Optional<Good> findByBoardAndUser(Board board, User user);

}
