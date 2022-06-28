package com.hanghae99_team3.model.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private String content;

    @Builder
    public CommentRequestDto(@NotNull String content) {
        this.content = content;
    }
}
