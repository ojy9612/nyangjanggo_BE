package com.hanghae99_team3.model.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SearchCondition {

    private Long id;
    private String resourceName;
    private String amount;
    private String category;
    private Status status;
    private Long boardId;

    public SearchCondition(Long id, String resourceName, String amount, String category, Status status, Long boardId) {
        this.id = id;
        this.resourceName = resourceName;
        this.amount = amount;
        this.category = category;
        this.status = status;
        this.boardId = boardId;
    }

}
