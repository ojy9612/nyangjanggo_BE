package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.exception.newException.IdDifferentException;
import com.hanghae99_team3.model.board.config.SaveCount;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hanghae99_team3.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;
    private final SaveCount saveCount;

    public Page<CommentResponseDto> getAllComment(Long boardId, Pageable pageable) {
        Board board = boardService.findBoardById(boardId);

        return commentRepository.findAllByBoard(board, pageable)
                .map(CommentResponseDto::new);

    }

    @Transactional
    public void createComment(PrincipalDetails principalDetails, CommentRequestDto commentRequestDto, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = boardService.findBoardById(boardId);

        Comment comment = Comment.builder()
                .commentRequestDto(commentRequestDto)
                .board(board)
                .user(user)
                .build();

        saveCount.appendBoardId(boardId);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(PrincipalDetails principalDetails, CommentRequestDto commentRequestDto, Long boardId, Long commentId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException(COMMENT_NOT_FOUND));

        if (!comment.getBoard().getId().equals(boardId)) throw new IdDifferentException(COMMENT_NOT_FOUND);
        if (!comment.getUser().getEmail().equals(user.getEmail())) throw new IdDifferentException(USER_ID_DIFFERENT);

        comment.updateContent(commentRequestDto);

    }

    public void removeComment(PrincipalDetails principalDetails, Long boardId, Long commentId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException(COMMENT_NOT_FOUND));

        if (!comment.getBoard().getId().equals(boardId)) throw new IdDifferentException(COMMENT_NOT_FOUND);
        if (!comment.getUser().getEmail().equals(user.getEmail())) throw new IdDifferentException(USER_ID_DIFFERENT);

        saveCount.appendBoardId(boardId);
        commentRepository.delete(comment);

    }

}