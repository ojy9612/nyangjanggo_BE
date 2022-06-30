package com.hanghae99_team3.model.images;

import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Images extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageLink;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Images(@NotNull String imageLink,@NotNull Board board) {
        this.imageLink = imageLink;
        board.addImages(this);
    }

}
