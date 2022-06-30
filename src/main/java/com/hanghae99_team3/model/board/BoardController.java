package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.BoardResponseDto;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/boards/{boardId}")
    public BoardResponseDto getOneBoard(@PathVariable Long boardId) {
        return new BoardResponseDto(boardService.getOneBoard(boardId));
    }

    @GetMapping("/api/boards")
    public Page<BoardResponseDto> getAllBoards(@PageableDefault(size = 3) Pageable pageable) {
        return boardService.getAllBoards(pageable);
    }


    @PostMapping("/api/board")
    public BoardResponseDto createBoard(@ModelAttribute BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return new BoardResponseDto(boardService.createBoard(boardRequestDto, principalDetails));
    }

    @PutMapping("/api/board/{boardId}")
    public BoardResponseDto updateBoard(@ModelAttribute BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PathVariable Long boardId) {

        return new BoardResponseDto(boardService.updateBoard(boardRequestDto, principalDetails, boardId));
    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @PathVariable Long boardId) {

        boardService.deleteBoard(principalDetails, boardId);
    }

}