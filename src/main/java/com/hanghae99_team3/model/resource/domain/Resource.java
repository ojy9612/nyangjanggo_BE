package com.hanghae99_team3.model.resource.domain;

import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class Resource extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String resourceName;

    @Column
    private String amount;

    @Column
    private String category;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Resource(@NotNull ResourceRequestDto resourceRequestDto,
                    @NotNull Board board) {
        this.resourceName = resourceRequestDto.getResourceName();
        this.amount = resourceRequestDto.getAmount();
        this.category = resourceRequestDto.getCategory();
        board.addResource(this);
    }

    public Resource(@NotNull ResourceRequestDto resourceRequestDto) {
        this.resourceName = resourceRequestDto.getResourceName();
        this.amount = resourceRequestDto.getAmount();
        this.category = resourceRequestDto.getCategory();
    }

}
