package com.hanghae99_team3.model.resource.repository;

import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;


public interface ResourceSearchRepository extends ElasticsearchRepository<ResourceKeywordDocument,Long> {

    Optional<ResourceKeywordDocument> findByResourceName(String resourceName);

    List<ResourceKeywordDocument> findAllByResourceNameAndCntGreaterThan(String resourceName, Integer cnt);

}
