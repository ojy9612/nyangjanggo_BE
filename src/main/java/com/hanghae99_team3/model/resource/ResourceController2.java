package com.hanghae99_team3.model.resource;

import com.hanghae99_team3.elasticsearch.SearchRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController2 {
    private final ResourceService2 service;


    @Autowired
    public ResourceController2(ResourceService2 service) {
        this.service = service;
    }

    @PostMapping
    public void index(@RequestBody final Resource resource) {
        service.index(resource);
    }


    @GetMapping("/{id}")
    public Resource getById(@PathVariable final String id) {
        return service.getByResourceName(id);
    }

    @PostMapping("/search")
    public List<Resource> search(@RequestBody final SearchRequestDto dto){
        return service.search(dto);
    }
}
