package com.hanghae99_team3.model.images;

import com.hanghae99_team3.model.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    List<Images> findAllByBoard(Board board);

    Optional<Images> findByImageLink(String imageLink);

}
