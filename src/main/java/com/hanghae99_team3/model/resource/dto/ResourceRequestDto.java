package com.hanghae99_team3.model.resource.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ResourceRequestDto {

    private String resourceName;
    private String amount;
    private String category;

    @Builder
    public ResourceRequestDto(@NotNull String resourceName,@NotNull String amount,@NotNull String category) {
        this.resourceName = resourceName;
        this.amount = amount;
        this.category = category;
    }
}
