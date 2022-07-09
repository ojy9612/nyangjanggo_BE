package com.hanghae99_team3.model.resource;

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
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String resourceName;

    @Column
    private String num;

    @Column
    private String category;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Resource(@NotNull ResourceRequestDto resourceRequestDto,
                    @NotNull Board board) {
        this.resourceName = resourceRequestDto.getResourceName();
        this.num = resourceRequestDto.getAmount();
        this.category = resourceRequestDto.getCategory();
        board.addResource(this);
    }

}
