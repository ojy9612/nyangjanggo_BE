package com.hanghae99_team3.model.resource.repository;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.resource.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findAllByBoard (Board board);
}
