package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class

CommentController {
    private final CommentService commentService;

    @GetMapping("/api/board/{boardId}/comments")
    public Page<CommentResponseDto> getAllComment(@PageableDefault(size = 3) @PathVariable Long boardId, Pageable pageable){
        return commentService.getAllComment(boardId, pageable);
    }



    @PostMapping("/api/board/{boardId}/comment")
    public CommentResponseDto createComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            @PathVariable Long boardId){

        return new CommentResponseDto(
                commentService.createComment(principalDetails,commentRequestDto,boardId)
        );
    }

    @PutMapping("/api/board/{boardId}/comment/{commentId}")
    public CommentResponseDto updateComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            @PathVariable Long boardId,
                                            @PathVariable Long commentId){

        return new CommentResponseDto(
                commentService.updateComment(principalDetails,commentRequestDto,boardId,commentId)
        );
    }

    @DeleteMapping("/api/board/{boardId}/comment/{commentId}")
    public void removeComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              @PathVariable Long boardId,
                              @PathVariable Long commentId){

        commentService.removeComment(principalDetails,boardId,commentId);
    }

}
