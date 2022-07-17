package com.hanghae99_team3.model.resource.repository;

import com.hanghae99_team3.model.resource.domain.ResourceDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ResourceSearchRepository extends ElasticsearchRepository<ResourceDocument,Long> {

    Page<ResourceDocument> findByCategory(String category, Pageable pageable);

    @Query("{\"match\": {\"resourcename.nori\": \"?0\"}}")
    List<ResourceDocument> findByResourcename(String searchWord);

    List<ResourceDocument> findAll();

}
