package com.hanghae99_team3.model.board.domain;


import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.comment.Comment;
import com.hanghae99_team3.model.good.Good;
import com.sun.istack.NotNull;
import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private String title;

    private String content;

    private String imgLink;
    private String imgKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Member member;

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
    public Board(@NotNull Member member, @NotNull String title, @NotNull String content, String imgLink, String imgKey) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.imgLink = imgLink;
        this.imgKey = imgKey;
    }
    public void update(@NotNull BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.imgLink = boardRequestDto.getImgLink();
        this.imgKey = boardRequestDto.getImgKey();

    }
}

