package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/board/{boardId}/comment")
    public List<CommentResponseDto> getAllComment(@PathVariable Long boardId){
        List<Comment> commentList = commentService.getAllComment(boardId);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment: commentList){
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        return commentResponseDtoList;
    }

    @PostMapping("/api/board/{boardId}/comment")
    public CommentResponseDto createComment(@AuthenticationPrincipal User userDetails,
                                            CommentRequestDto commentRequestDto,
                                            @PathVariable Long boardId){

        return new CommentResponseDto(
                commentService.createComment(userDetails,commentRequestDto,boardId)
        );
    }

    @PutMapping("/api/board/{boardId}/comment/{commentId}")
    public CommentResponseDto updateComment(@AuthenticationPrincipal User userDetails,
                                            CommentRequestDto commentRequestDto,
                                            @PathVariable Long boardId,
                                            @PathVariable Long commentId){

        return new CommentResponseDto(
                commentService.updateComment(userDetails,commentRequestDto,boardId,commentId)
        );
    }

    @DeleteMapping("/api/board/{boardId}/comment/{commentId}")
    public void removeComment(@AuthenticationPrincipal User userDetails,
                              @PathVariable Long boardId,
                              @PathVariable Long commentId){

        commentService.removeComment(userDetails,boardId,commentId);
    }

}
