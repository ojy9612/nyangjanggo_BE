package com.hanghae99_team3.model.board.domain;

import com.hanghae99_team3.model.resource.domain.Resource;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ResourceInBoard {

    private Long id;
    private String resourceName;
    private String amount;
    private String category;
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ResourceInBoard(Resource resource) {
        this.id = resource.getId();
        this.resourceName = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
        this.boardId = resource.getBoard().getId();
        this.createdAt = resource.getCreatedAt();
        this.modifiedAt = resource.getModifiedAt();
    }

}
