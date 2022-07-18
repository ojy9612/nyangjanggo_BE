package com.hanghae99_team3.model.fridge.dto;

import com.hanghae99_team3.model.fridge.Fridge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FridgeResponseDto {

    private String resourceName;
    private String amount;
    private String category;
    private String endTime;

    @Builder
    public FridgeResponseDto(Fridge fridge) {
        this.resourceName = fridge.getResourceName();
        this.amount = fridge.getAmount();
        this.category = fridge.getCategory();
        this.endTime = fridge.getEndTime();
    }

}
