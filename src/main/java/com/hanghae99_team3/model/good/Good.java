package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.TimestampedOnlyCreated;
import com.hanghae99_team3.model.board.Board;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Good extends TimestampedOnlyCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

//    private Member member;

    @Builder
    public Good(@NotNull Board board) {
        board.addGood(this);
    }

}
