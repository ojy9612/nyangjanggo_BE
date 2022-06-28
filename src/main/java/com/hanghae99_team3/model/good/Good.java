package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.TimestampedOnlyCreated;
import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.member.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Good(@NotNull Board board, @NotNull User user) {
        board.addGood(this);
        user.addGood(this);
    }

}
