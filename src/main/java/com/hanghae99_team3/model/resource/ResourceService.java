package com.hanghae99_team3.model.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public void createResource(List<String> resourceInfos){

    }

}
