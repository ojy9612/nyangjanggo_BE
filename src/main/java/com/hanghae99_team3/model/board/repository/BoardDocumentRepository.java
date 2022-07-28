package com.hanghae99_team3.model.board.repository;

import com.hanghae99_team3.model.board.domain.BoardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BoardDocumentRepository extends ElasticsearchRepository<BoardDocument, Long> {

    List<BoardDocument> findAllByIdIn(List<Long> boardIdSet);

    List<BoardDocument> findFirst2By();

}
