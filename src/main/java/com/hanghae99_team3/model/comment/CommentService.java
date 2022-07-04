package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.board.BoardService;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.hanghae99_team3.exception.ErrorMessage.COMMENT_NOT_FOUND;
import static com.hanghae99_team3.exception.ErrorMessage.ID_DUPLICATE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    public Page<CommentResponseDto> getAllComment(Long boardId, Pageable pageable) {
        Board board = boardService.findBoardById(boardId);

        return commentRepository.findAllByBoard(board,pageable)
                .map(CommentResponseDto::new);



    }

    @Transactional
    public Comment createComment(PrincipalDetails principalDetails, CommentRequestDto commentRequestDto, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = boardService.findBoardById(boardId);

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .board(board)
                .user(user)
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(PrincipalDetails principalDetails, CommentRequestDto commentRequestDto, Long boardId, Long commentId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = boardService.findBoardById(boardId);

        if (board.getUser().getEmail().equals(user.getEmail()) ){
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    ()-> new IllegalArgumentException(COMMENT_NOT_FOUND));

            comment.updateContent(commentRequestDto);
            return commentRepository.save(comment);
        } else throw new IdDuplicateException(ID_DUPLICATE);

    }

    public void removeComment(PrincipalDetails principalDetails, Long boardId, Long commentId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = boardService.findBoardById(boardId);

        if (board.getUser().getEmail().equals(user.getEmail()) ){
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    ()-> new IllegalArgumentException(COMMENT_NOT_FOUND));

            commentRepository.delete(comment);
        } else throw new IdDuplicateException(ID_DUPLICATE);

    }

}