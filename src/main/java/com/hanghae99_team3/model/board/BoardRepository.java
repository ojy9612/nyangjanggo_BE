package com.hanghae99_team3.model.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
