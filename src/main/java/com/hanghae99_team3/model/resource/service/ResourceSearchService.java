package com.hanghae99_team3.model.resource.service;

import com.hanghae99_team3.model.resource.domain.ResourceDocument;
import com.hanghae99_team3.model.resource.dto.ResourceDocumentResponseDto;
import com.hanghae99_team3.model.resource.repository.ResourceRepository;
import com.hanghae99_team3.model.resource.repository.ResourceSearchQueryRepository;
import com.hanghae99_team3.model.resource.dto.ResourceResponseDto;
import com.hanghae99_team3.model.resource.dto.SearchCondition;
import com.hanghae99_team3.model.resource.repository.ResourceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceSearchService {

    private final ResourceRepository resourceRepository;
    private final ResourceSearchRepository resourceSearchRepository;
    private final ResourceSearchQueryRepository resourceSearchQueryRepository;

    public Set<Long> getByResourceName(String searchWord) {
        return resourceSearchRepository.findByResourcename(searchWord)
                .stream().map(ResourceDocument::getBoard_id).collect(Collectors.toSet());
    }

    public Page<ResourceDocumentResponseDto> getByCategory(String category, Pageable pageable) {
        return resourceSearchRepository.findByCategory(category,pageable)
                .map(ResourceDocumentResponseDto::new);
    }

    public List<ResourceDocumentResponseDto> searchByCondition(SearchCondition searchCondition, Pageable pageable) {
        return resourceSearchQueryRepository.findByCondition(searchCondition, pageable)
                .stream()
                .map(ResourceDocumentResponseDto::new)
                .collect(Collectors.toList());
    }


}