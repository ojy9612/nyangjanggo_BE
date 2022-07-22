package com.hanghae99_team3.model.board;

import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.dto.response.BoardResponseDto;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.repository.BoardSearchRepository;
import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.resource.repository.ResourceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class testController {

    private final BoardSearchRepository boardSearchRepository;
    private final BoardRepository boardRepository;
    private final ResourceSearchRepository resourceSearchRepository;


    @GetMapping("/api/boards/elastic")
    public Page<BoardResponseDto> getAllBoardDocument(Pageable pageable){
        List<Long> boardIdList = boardSearchRepository.findFirst2By().stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList, pageable).map(BoardResponseDto::new);
    }
    @GetMapping("/api/resources/elastic")
    public List<ResourceKeywordDocument> getAllResourceKeywordDocument(Pageable pageable){

        return resourceSearchRepository.findAll();
    }

    @PostMapping("/test/resources")
    public void createManyResources(@RequestBody TestWrapper resources){
        List<ResourceKeywordDocument> resourceKeywordDocumentList = new ArrayList<>();

        resources.getResources().forEach(resource -> {
            Resource resource1 = new Resource (
                    ResourceRequestDto.builder().resourceName(resource).amount("1").category("카테고리").build()
            );

            ResourceKeywordDocument resourceKeywordDocument = ResourceKeywordDocument.builder()
                    .resource(resource1)
                    .build();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocumentList.add(resourceKeywordDocument);
        });

        resourceSearchRepository.saveAll(resourceKeywordDocumentList);
    }
}

