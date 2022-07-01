package com.hanghae99_team3.model.recipestep;


import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class RecipeStep extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer stepNum;
    @Column
    private String content;

    @Column
    private String imageLink;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public RecipeStep(@NotNull Integer stepNum,@NotNull String content,@NotNull String imageLink,@NotNull Board board) {
        this.stepNum = stepNum;
        this.content = content;
        this.imageLink = imageLink;
        board.addRecipeStep(this);
    }

}
