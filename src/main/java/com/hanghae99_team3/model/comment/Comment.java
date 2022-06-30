package com.hanghae99_team3.model.comment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

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
    public Comment(@NotNull String content, @NotNull Board board, @NotNull User user) {
        this.content = content;
        board.addComment(this);
        user.addComment(this);
    }

    public void updateContent(@NotNull CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
    }

}
