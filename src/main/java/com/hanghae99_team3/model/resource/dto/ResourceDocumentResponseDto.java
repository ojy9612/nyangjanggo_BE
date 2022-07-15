package com.hanghae99_team3.model.resource.dto;

import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.domain.ResourceDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ResourceDocumentResponseDto {

    private String resourcename;
    private String amount;
    private String category;
    private Long boardId;

    @Builder
    public ResourceDocumentResponseDto(@NotNull ResourceDocument resourceDocument) {
        this.resourcename = resourceDocument.getResourcename();
        this.amount = resourceDocument.getAmount();
        this.category = resourceDocument.getCategory();
        this.boardId = resourceDocument.getBoard_id();
    }

}
