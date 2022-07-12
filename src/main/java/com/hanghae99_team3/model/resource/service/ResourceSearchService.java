package com.hanghae99_team3.model.resource.service;

import com.hanghae99_team3.model.resource.repository.ResourceRepository;
import com.hanghae99_team3.model.resource.repository.ResourceSearchQueryRepository;
import com.hanghae99_team3.model.resource.dto.ResourceResponseDto;
import com.hanghae99_team3.model.resource.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceSearchService {

//    private final ResourceRepository resourceRepository;
//    private final ResourceSearchRepository resourceSearchRepository;
//    private final ResourceSearchQueryRepository resourceSearchQueryRepository;
//
//    public List<ResourceResponseDto> getByResourceName(String resourceName, Pageable pageable) {
//        return resourceSearchRepository.findByResourceName(resourceName, pageable)
//                .stream()
//                .map(ResourceResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    public List<ResourceResponseDto> searchByCondition(SearchCondition searchCondition, Pageable pageable) {
//        return resourceSearchQueryRepository.findByCondition(searchCondition, pageable)
//                .stream()
//                .map(ResourceResponseDto::new)
//                .collect(Collectors.toList());
//    }

}