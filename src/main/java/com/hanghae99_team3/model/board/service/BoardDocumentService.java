package com.hanghae99_team3.model.board.service;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.repository.BoardDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDocumentService {

    private final BoardDocumentRepository boardDocumentRepository;


    public void createBoard(Board board) {
        BoardDocument boardDocument = BoardDocument.builder()
                .board(board)
                .build();

        boardDocumentRepository.save(boardDocument);
    }

    public void updateBoard(Board board) {
        BoardDocument boardDocument = boardDocumentRepository.findById(board.getId()).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        boardDocument.updateBoardDocument(board);

        boardDocumentRepository.save(boardDocument);
    }

    public void deleteBoard(Board board) {
        boardDocumentRepository.deleteById(board.getId());
    }


}
