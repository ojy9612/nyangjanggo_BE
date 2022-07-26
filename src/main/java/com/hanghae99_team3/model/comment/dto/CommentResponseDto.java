package com.hanghae99_team3.model.comment.dto;

import com.hanghae99_team3.model.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String nickname;
    private String userImg;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(@NotNull Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.userImg = comment.getUser().getUserImg();
        this.comment = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
