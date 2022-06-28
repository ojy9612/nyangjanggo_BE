package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.domain.Board;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

//    private User user;

    @Builder
    public Comment(@NotNull String content,@NotNull Board board) {
        this.content = content;
        board.addComment(this);
    }

    public void updateContent(@NotNull CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
    }

}
