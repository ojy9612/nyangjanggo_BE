package com.hanghae99_team3.model.board.repository;

import com.hanghae99_team3.model.board.domain.BoardDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Set;

public interface BoardDocumentRepository extends ElasticsearchRepository<BoardDocument,Long> {

    List<BoardDocument> findAllByIdIn(List<Long> boardIdSet);
    List<BoardDocument> findFirst2By();

    List<BoardDocument> findByTitle(String title);

    @Query(value = "{\"match\": {\"resourceDocumentList.resourceName\": \"?0\"}}")
    List<BoardDocument> searchByResourceNameWords(String ResourceNameWords);
}
