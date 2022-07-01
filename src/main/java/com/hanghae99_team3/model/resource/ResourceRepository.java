package com.hanghae99_team3.model.resource;

import com.hanghae99_team3.model.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    void deleteAllByBoard(Board board);
}
