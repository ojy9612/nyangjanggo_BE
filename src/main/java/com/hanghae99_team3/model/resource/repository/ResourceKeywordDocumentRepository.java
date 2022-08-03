package com.hanghae99_team3.model.resource.repository;

import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;


public interface ResourceKeywordDocumentRepository extends ElasticsearchRepository<ResourceKeywordDocument, Long> {


    @NotNull List<ResourceKeywordDocument> findAll();

    @Query("{\"match\": {\"resourceName.keyword\": \"?0\"}}}")
    Optional<ResourceKeywordDocument> findByResourceNameKeyword(String resourceName);

}
