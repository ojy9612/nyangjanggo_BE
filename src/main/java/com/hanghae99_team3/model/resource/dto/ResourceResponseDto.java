package com.hanghae99_team3.model.resource.dto;

import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.domain.ResourceDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ResourceResponseDto {

    private String resourcename;
    private String amount;
    private String category;

    @Builder(builderMethodName = "Resource")
    public ResourceResponseDto(@NotNull Resource resource) {
        this.resourcename = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
    }
    @Builder(builderMethodName = "ResourceDocument")
    public ResourceResponseDto(@NotNull ResourceDocument resourceDocument) {
        this.resourcename = resourceDocument.getResourcename();
        this.amount = resourceDocument.getAmount();
        this.category = resourceDocument.getCategory();
    }

}
