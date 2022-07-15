package com.hanghae99_team3.model.resource.repository;

import com.hanghae99_team3.model.resource.domain.ResourceDocument;
import com.hanghae99_team3.model.resource.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ResourceSearchQueryRepository {

    private final ElasticsearchOperations operations;

    public List<ResourceDocument> findByCondition(SearchCondition searchCondition, Pageable pageable) {
        CriteriaQuery query = createConditionCriteriaQuery(searchCondition).setPageable(pageable);

        SearchHits<ResourceDocument> search = operations.search(query, ResourceDocument.class);
        return search.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private CriteriaQuery createConditionCriteriaQuery(SearchCondition searchCondition) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (searchCondition == null)
            return query;

        if (searchCondition.getId() != null)
            query.addCriteria(Criteria.where("id").is(searchCondition.getId()));

        if(StringUtils.hasText(searchCondition.getResourcename()))
            query.addCriteria(Criteria.where("resourcename").is(searchCondition.getResourcename()));

        if(StringUtils.hasText(searchCondition.getAmount()))
            query.addCriteria(Criteria.where("amount").is(searchCondition.getAmount()));

        if(StringUtils.hasText(searchCondition.getCategory()))
            query.addCriteria(Criteria.where("category").is(searchCondition.getCategory()));

        if(searchCondition.getBoardId() != null)
            query.addCriteria(Criteria.where("board_id").is(searchCondition.getBoardId()));

        return query;
    }
}