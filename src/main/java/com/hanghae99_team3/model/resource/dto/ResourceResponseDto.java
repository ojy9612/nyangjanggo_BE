package com.hanghae99_team3.model.resource.dto;

import com.hanghae99_team3.model.resource.Resource;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ResourceResponseDto {

    private String resourceName;
    private String amount;
    private String category;

    @Builder
    public ResourceResponseDto(@NotNull Resource resource) {
        this.resourceName = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
    }

}
