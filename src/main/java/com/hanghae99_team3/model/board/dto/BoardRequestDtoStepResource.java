package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardRequestDtoStepResource {

    private Long boardId;
    private List<ResourceRequestDto> resourceRequestDtoList;

    @Builder
    public BoardRequestDtoStepResource(Long boardId,
                                       List<ResourceRequestDto> resourceRequestDtoList ) {
        this.boardId = boardId;
        this.resourceRequestDtoList = resourceRequestDtoList;
    }

}
