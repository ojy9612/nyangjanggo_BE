package com.hanghae99_team3.model.resource.service;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.resource.repository.ResourceRepository;
import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public void createResource(List<ResourceRequestDto> resourceRequestDtoList, Board board){

        resourceRequestDtoList.forEach(resourceRequestDto -> {
            Resource resource = Resource.builder()
                    .resourceRequestDto(resourceRequestDto)
                    .board(board)
                    .build();

            resourceRepository.save(resource);
        });
    }

    public void updateResource(List<ResourceRequestDto> resourceRequestDtoList, Board board){
        this.removeAllResource(board);
        this.createResource(resourceRequestDtoList,board);
    }

    public void removeAllResource(Board board) {
        resourceRepository.deleteAll(resourceRepository.findAllByBoard(board));
    }
}
