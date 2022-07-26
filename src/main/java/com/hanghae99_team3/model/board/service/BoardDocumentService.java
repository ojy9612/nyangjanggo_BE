package com.hanghae99_team3.model.board.service;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.repository.BoardDocumentRepository;
import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import com.hanghae99_team3.model.resource.repository.ResourceSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDocumentService {

    private final BoardDocumentRepository boardDocumentRepository;
    private final BoardRepository boardRepository;
    private final ResourceSearchRepository resourceSearchRepository;

    public Page<Board> getBoardDocumentsByResource(String resourceNameWords, Pageable pageable) {

        List<Long> boardIdList = boardDocumentRepository.searchByResourceNameWords(resourceNameWords).stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList,pageable);
    }

    public Page<Board> getBoardDocumentsByTitle(String titleWords, Pageable pageable) {

        List<Long> boardIdList = boardDocumentRepository.findByTitle(titleWords).stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList,pageable);
    }

    public List<String> recommendResource(String resourceName) {

        return resourceSearchRepository.findAllByResourceNameAndCntGreaterThan(resourceName, 2).stream()
                .map(ResourceKeywordDocument::getResourceName).collect(Collectors.toList());
    }

    public List<String> recommendBoardDocumentByTitle(String titleWords) {

        return boardDocumentRepository.findByTitle(titleWords).stream()
                .map(BoardDocument::getTitle).collect(Collectors.toList());
    }


    public void createBoard(Board board){
        BoardDocument boardDocument = BoardDocument.builder()
                .board(board)
                .build();

        boardDocumentRepository.save(boardDocument);
    }

    public void updateBoard(Board board) {
        this.deleteBoard(board);
        this.createBoard(board);
    }

    public void deleteBoard(Board board){
        boardDocumentRepository.deleteById(board.getId());
    }


}
