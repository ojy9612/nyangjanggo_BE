package com.hanghae99_team3.model.resource.dto;

import lombok.Getter;

@Getter
public class SearchCondition {

    private final Long id;
    private final String resourcename;
    private final String amount;
    private final String category;
    private final Long boardId;

    public SearchCondition(Long id, String resourcename, String amount, String category, Long boardId) {
        this.id = id;
        this.resourcename = resourcename;
        this.amount = amount;
        this.category = category;
        this.boardId = boardId;
    }

}
