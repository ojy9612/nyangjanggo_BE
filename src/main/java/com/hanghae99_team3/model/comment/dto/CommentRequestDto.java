package com.hanghae99_team3.model.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class CommentRequestDto {

    private String content;

    @Builder
    public CommentRequestDto(@NotNull String content) {
        this.content = content;
    }
}
