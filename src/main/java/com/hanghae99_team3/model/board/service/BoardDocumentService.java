package com.hanghae99_team3.model.board.service;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.repository.BoardSearchRepository;
import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import com.hanghae99_team3.model.resource.repository.ResourceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDocumentService {

    private final BoardSearchRepository boardSearchRepository;
    private final BoardRepository boardRepository;
    private final ResourceSearchRepository resourceSearchRepository;

    public void createBoard(Board board){
        BoardDocument boardDocument = BoardDocument.builder()
                .board(board)
                .build();

        boardSearchRepository.save(boardDocument);
    }

    public Page<Board> getAllBoardDocument(Pageable pageable){
        List<Long> boardIdList = boardSearchRepository.findAll().stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList, pageable);
    }

    public Page<Board> getBoardDocumentsByTitle(String titleWords, Pageable pageable) {

        List<Long> boardIdList = boardSearchRepository.findByTitle(titleWords).stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList,pageable);
    }

    public Set<String> recommendBoardDocumentByTitle(String titleWords) {

        return boardSearchRepository.findByTitle(titleWords).stream()
                .map(BoardDocument::getTitle).collect(Collectors.toSet());
    }

    public Page<Board> getBoardDocumentsByResource(String resourceNameWords, Pageable pageable) {

        List<Long> boardIdList = boardSearchRepository.searchByResourceNameWords(resourceNameWords).stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList,pageable);
    }

    public List<String> recommendResource(String resourceName) {

        return resourceSearchRepository.findAllByResourceNameAndCntGreaterThan(resourceName, 2).stream()
                .map(ResourceKeywordDocument::getResourceName).collect(Collectors.toList());
    }

//    public Page<Board> getByResource(String searchWord,Pageable pageable) {
//
//        Set<Long> boardIdSet = resourceSearchService.getByResourceName(searchWord);
//
//        return boardRepository.findAllByIdIn(boardIdSet,pageable);
//    }

}
