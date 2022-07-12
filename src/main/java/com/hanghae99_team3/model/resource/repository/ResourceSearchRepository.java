package com.hanghae99_team3.model.resource.repository;

import com.hanghae99_team3.model.resource.domain.ResourceDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ResourceSearchRepository extends ElasticsearchRepository<ResourceDocument,Long> {

    List<ResourceDocument> findByResourceName(String resourceName, Pageable pageable);

    List<ResourceDocument> findByCategory(String category, Pageable pageable);

}
