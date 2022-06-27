package com.hanghae99_team3.model.board.controller;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.BoardResponseDto;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;


    @GetMapping("/api/boards/{boardId}")
    public BoardResponseDto getOneBoard(@PathVariable Long boardId) {
        return new BoardResponseDto(
                boardService.getOneBoard(boardId)
        );
    }

    @GetMapping("/api/boards")
    public List<BoardResponseDto> getAllBoards() {
        List<Board> boardList = boardService.getAllBoards();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }
        return boardResponseDtoList;

    }


    @PostMapping("/api/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal Member member) {


        return new BoardResponseDto(
                boardService.createBoard(boardRequestDto, member)
        );

    }

    @PutMapping("/api/board/{boardId}")
    public BoardResponseDto updateBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal Member member, @PathVariable Long boardId) {

        return new BoardResponseDto(
                boardService.updateBoard(boardRequestDto, member, boardId)
        );
    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal Member member, @PathVariable Long boardId) {
        boardService.deleteBoard(member, boardId);

    }
}