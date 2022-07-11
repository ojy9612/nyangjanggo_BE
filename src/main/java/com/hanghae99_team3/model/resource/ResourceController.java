package com.hanghae99_team3.model.resource;

import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceRepository resourceRepository;

    @PostMapping("/resource")
    public void aa(@RequestBody ResourceRequestDto resourceRequestDto){

        Resource resource = new Resource(resourceRequestDto);
        resourceRepository.save(resource);
    }


}
