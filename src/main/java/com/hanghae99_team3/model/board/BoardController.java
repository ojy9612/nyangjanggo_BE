package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.board.dto.*;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/boards/{boardId}")
    public BoardDetailResponseDto getOneBoard(@PathVariable Long boardId) {
        return new BoardDetailResponseDto(boardService.getOneBoard(boardId));
    }

    @GetMapping("/api/boards")
    public Page<BoardResponseDto> getAllBoards(@PageableDefault(size = 3) Pageable pageable) {
        return boardService.getAllBoards(pageable);
    }


    @PostMapping(value = "/api/board/step/0")
    public BoardResponseDto createBoardStepStart(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return new BoardResponseDto(
                boardService.createBoardStepStart(principalDetails)
        );
    }

    @PostMapping(value = "/api/board/step/1")
    public void createBoardStepMain(@RequestBody BoardRequestDtoStepMain boardRequestDtoStepMain,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.createBoardStepMain(boardRequestDtoStepMain, principalDetails);

    }
    @PostMapping(value = "/api/board/step/2")
    public void createBoardStepResource(@RequestBody BoardRequestDtoStepResource boardRequestDtoStepResource,
                                                    @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.createBoardStepResource(boardRequestDtoStepResource, principalDetails);

    }
    @PostMapping(value = "/api/board/step/3")
    public void createBoardStepRecipe(@RequestBody BoardRequestDtoStepRecipe boardRequestDtoStepRecipe,
                                                  @RequestPart MultipartFile multipartFile,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.createBoardStepRecipe(boardRequestDtoStepRecipe, multipartFile, principalDetails);

    }

    @PostMapping(value = "/api/board/step/-1")
    public void createBoardStepEnd(@RequestParam Long boardId,@AuthenticationPrincipal PrincipalDetails principalDetails) {


        boardService.createBoardStepEnd(boardId, principalDetails);

    }

//    @PutMapping("/api/board/{boardId}")
//    public BoardResponseDto updateBoard(@ModelAttribute BoardRequestDtoStep0 boardRequestDtoStepZero,
//                                        @AuthenticationPrincipal PrincipalDetails principalDetails,
//                                        @PathVariable Long boardId) {
//
//        return new BoardResponseDto(boardService.updateBoard(boardRequestDtoStepZero, principalDetails, boardId));
//    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @PathVariable Long boardId) {

        boardService.deleteBoard(principalDetails, boardId);
    }

}