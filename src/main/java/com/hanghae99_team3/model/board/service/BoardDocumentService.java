package com.hanghae99_team3.model.board.service;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.repository.BoardSearchRepository;
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

    private final BoardSearchRepository boardSearchRepository;
    private final BoardRepository boardRepository;

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

//    public Page<Board> getByResource(String searchWord,Pageable pageable) {
//
//        Set<Long> boardIdSet = resourceSearchService.getByResourceName(searchWord);
//
//        return boardRepository.findAllByIdIn(boardIdSet,pageable);
//    }

}
