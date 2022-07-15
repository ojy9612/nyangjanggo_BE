package com.hanghae99_team3.model.comment;


import com.hanghae99_team3.model.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;




public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoard(Board board, Pageable pageable);
}
