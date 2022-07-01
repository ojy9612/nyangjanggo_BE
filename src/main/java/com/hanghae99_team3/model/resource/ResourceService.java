package com.hanghae99_team3.model.resource;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    public void removeResource(Board board) {
        resourceRepository.deleteAllByBoard(board);
    }
}
