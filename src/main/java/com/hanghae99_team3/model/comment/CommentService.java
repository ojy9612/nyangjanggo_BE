package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.board.BoardRepository;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.member.UserRepository;
import com.hanghae99_team3.model.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.hanghae99_team3.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<Comment> getAllComment(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        // TODO : 페이징 처리 후 넘기기
        return board.getCommentList();
    }

    @Transactional
    public Comment createComment(User userDetails, CommentRequestDto commentRequestDto, Long boardId) {
        User user = userDetails;
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .board(board)
                .user(user)
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(User userDetails, CommentRequestDto commentRequestDto, Long boardId, Long commentId) {
        User user = userDetails;
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        if (board.getUser().getUsername().equals(user.getUsername()) ){
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    ()-> new IllegalArgumentException(COMMENT_NOT_FOUND));

            comment.updateContent(commentRequestDto);
            return commentRepository.save(comment);
        } else throw new IdDuplicateException(ID_DUPLICATE);

    }

    public void removeComment(User userDetails, Long boardId, Long commentId) {
        User user = userDetails;
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        if (board.getUser().getUsername().equals(user.getUsername()) ){
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    ()-> new IllegalArgumentException(COMMENT_NOT_FOUND));

            commentRepository.delete(comment);
        } else throw new IdDuplicateException(ID_DUPLICATE);

    }

}