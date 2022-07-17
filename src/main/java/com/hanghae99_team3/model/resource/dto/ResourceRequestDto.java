package com.hanghae99_team3.model.resource.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ResourceRequestDto {

    private String resourcename;
    private String amount;
    private String category;

    @Builder
    public ResourceRequestDto(@NotNull String resourcename, @NotNull String amount, @NotNull String category) {
        this.resourcename = resourcename;
        this.amount = amount;
        this.category = category;
    }
}
