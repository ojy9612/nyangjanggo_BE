package com.hanghae99_team3.model.comment;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private String content;

    @Builder
    public CommentRequestDto(@NotNull String content) {
        this.content = content;
    }
}
