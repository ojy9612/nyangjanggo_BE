package com.hanghae99_team3.model.resource.service;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import com.hanghae99_team3.model.resource.repository.ResourceRepository;
import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.resource.repository.ResourceKeywordDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceKeywordDocumentRepository resourceKeywordDocumentRepository;

    public void createResource(List<ResourceRequestDto> resourceRequestDtoList, Board board) {

        List<Resource> resourceList = new ArrayList<>();
        resourceRequestDtoList.forEach(resourceRequestDto -> {
            Resource resource = Resource.builder()
                    .resourceRequestDto(resourceRequestDto)
                    .board(board)
                    .build();

            Optional<ResourceKeywordDocument> optionalResourceKeywordDocument =
                    resourceKeywordDocumentRepository.findByResourceNameKeyword(resource.getResourceName());

            if (optionalResourceKeywordDocument.isPresent()) {
                ResourceKeywordDocument resourceKeywordDocument = optionalResourceKeywordDocument.get();
                resourceKeywordDocument.plusCnt();
                resourceKeywordDocumentRepository.save(resourceKeywordDocument);
            } else {
                resourceKeywordDocumentRepository.save(ResourceKeywordDocument.builder()
                        .resource(resource)
                        .build()
                );
            }

            resourceList.add(resource);
        });
        resourceRepository.saveAll(resourceList);
    }

    // 사용자가 내용을 한번에 수정할 가능성이 높으므로 항상 데이터를 List 로 받게 함
    public void updateResource(List<ResourceRequestDto> resourceRequestDtoList, Board board) {
        this.removeAllResource(board);
        this.createResource(resourceRequestDtoList, board);
    }

    public void removeAllResource(Board board) {
        List<String> resourceNameList = board.getResourceList().stream()
                .map(Resource::getResourceName).collect(Collectors.toList());

        List<ResourceKeywordDocument> resourceKeywordDocumentList = new ArrayList<>();
        resourceNameList.forEach(s -> resourceKeywordDocumentRepository.findByResourceNameKeyword(s).ifPresent(resourceKeywordDocument -> {
                    resourceKeywordDocument.minusCnt();
                    if (resourceKeywordDocument.getCnt() <= 0) {
                        resourceKeywordDocumentList.add(resourceKeywordDocument);
                    }
                }
        ));
        resourceKeywordDocumentRepository.deleteAll(resourceKeywordDocumentList);

        resourceRepository.deleteAll(resourceRepository.findAllByBoard(board));
    }

}
