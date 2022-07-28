package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Comment(@NotNull CommentRequestDto commentRequestDto, @NotNull Board board, @NotNull User user) {
        this.content = commentRequestDto.getContent();
        board.addComment(this);
        user.addComment(this);
    }

    public void updateContent(@NotNull CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

}
