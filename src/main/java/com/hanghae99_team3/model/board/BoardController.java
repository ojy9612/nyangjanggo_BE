package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.BoardResponseDto;
import com.hanghae99_team3.model.user.domain.User;
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
        return new BoardResponseDto(boardService.getOneBoard(boardId));
    }

    @GetMapping("/api/boards")
    public List<BoardResponseDto> getAllBoards() {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        boardService.getAllBoards().forEach(board -> {
            boardResponseDtoList.add(new BoardResponseDto(board));
        });

        return boardResponseDtoList;
    }


    @PostMapping("/api/board")
    public BoardResponseDto createBoard(@ModelAttribute BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal User user) {

        return new BoardResponseDto(boardService.createBoard(boardRequestDto, user));
    }

    @PutMapping("/api/board/{boardId}")
    public BoardResponseDto updateBoard(@ModelAttribute BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal User user,
                                        @PathVariable Long boardId) {

        return new BoardResponseDto(boardService.updateBoard(boardRequestDto, user, boardId));
    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal User user,
                            @PathVariable Long boardId) {

        boardService.deleteBoard(user, boardId);
    }

}