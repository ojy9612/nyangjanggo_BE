package com.hanghae99_team3.model.recipestep;

import com.hanghae99_team3.model.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    List<RecipeStep> findAllByBoard(Board board);

}
