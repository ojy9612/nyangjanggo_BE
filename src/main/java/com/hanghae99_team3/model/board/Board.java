package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.comment.Comment;
import com.hanghae99_team3.model.good.Good;
import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.user.domain.User;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", nullable = false)
    private  Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String imgLink;
    @Column
    private String imgKey;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "boards", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "boards", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Good> goodList = new ArrayList<>();

    public void addComment(Comment comment) {
        comment.setBoard(this);
        this.commentList.add(comment);
    }

    public void addGood(Good good) {
        good.setBoard(this);
        this.goodList.add(good);
    }

    @Builder
    public Board(@NotNull User user, @NotNull String title, @NotNull String content) {
        user.addBoard(this);
        this.title = title;
        this.content = content;
    }
    public void update(@NotNull BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();

    }
}

