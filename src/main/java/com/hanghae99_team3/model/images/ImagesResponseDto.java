package com.hanghae99_team3.model.images;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImagesResponseDto {

    private String imgLink;

    public ImagesResponseDto(Images images) {
        this.imgLink = images.getImageLink();
    }

}
