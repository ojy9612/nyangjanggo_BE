package com.hanghae99_team3.model.fridge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FridgeRequestDto {

    private String resourceName;

    private String amount;

    private String category;

    private String endTime;

    @Builder
    public FridgeRequestDto(String resourceName, String amount, String category, String endTime) {
        this.resourceName = resourceName;
        this.amount = amount;
        this.category = category;
        this.endTime = endTime;
    }
}
