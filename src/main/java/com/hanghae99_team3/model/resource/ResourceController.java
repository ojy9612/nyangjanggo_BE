package com.hanghae99_team3.model.resource;

import com.hanghae99_team3.model.resource.repository.ResourceRepository;
import com.hanghae99_team3.model.resource.service.ResourceSearchService;
import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.resource.dto.ResourceResponseDto;
import com.hanghae99_team3.model.resource.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResourceController {

//    private final ResourceRepository resourceRepository;
//    private final ResourceSearchService resourceSearchService;
//
//    @PostMapping("/resource")
//    public void aa(@RequestBody ResourceRequestDto resourceRequestDto){
//
//        Resource resource = new Resource(resourceRequestDto);
//        resourceRepository.save(resource);
//    }
//
//    @GetMapping("/resources/resourceName")
//    public List<ResourceResponseDto> searchByNickname(@RequestParam String resourceName, Pageable pageable){
//
//        return resourceSearchService.getByResourceName(resourceName,pageable);
//    }
//
//
//    @GetMapping("/resources")
//    public List<ResourceResponseDto> searchByName(SearchCondition searchCondition, Pageable pageable){
//
//        return resourceSearchService.searchByCondition(searchCondition,pageable);
//    }

}
