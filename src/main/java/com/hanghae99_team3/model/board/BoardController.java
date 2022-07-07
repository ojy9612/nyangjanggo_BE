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

        return boardService.createBoardStepStart(principalDetails)
                .map(BoardResponseDto::new).orElse(null);

    }

    @PostMapping(value = "/api/board/step/1")
    public Long createBoardStepMain(@RequestPart BoardRequestDtoStepMain boardRequestDtoStepMain,
                                    @RequestPart MultipartFile multipartFile,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.createBoardStepMain(boardRequestDtoStepMain, multipartFile, principalDetails);

    }

    @PostMapping(value = "/api/board/step/2")
    public Long createBoardStepResource(@RequestBody BoardRequestDtoStepResource boardRequestDtoStepResource,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.createBoardStepResource(boardRequestDtoStepResource, principalDetails);

    }

    @PostMapping(value = "/api/board/step/3")
    public Long createBoardStepRecipe(@RequestPart BoardRequestDtoStepRecipe boardRequestDtoStepRecipe,
                                      @RequestPart MultipartFile multipartFile,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.createBoardStepRecipe(boardRequestDtoStepRecipe, multipartFile, principalDetails);

    }

    @PostMapping(value = "/api/board/step/-1")
    public void createBoardStepEnd(@RequestPart Long boardId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.createBoardStepEnd(boardId, principalDetails);

    }
//
    @PutMapping("/api/board/step/1")
    public Long updateBoardStepMain(@RequestPart Long boardId,
                                    @RequestPart BoardRequestDtoStepMain boardRequestDtoStepMain,
                                    @RequestPart MultipartFile multipartFile,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.updateBoardStepMain(boardRequestDtoStepMain, multipartFile, principalDetails,boardId);
    }

    @PutMapping("/api/board/step/2")
    public Long updateBoardStepResource(@RequestPart BoardRequestDtoStepResource boardRequestDtoStepResource,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.updateBoardStepResource(boardRequestDtoStepResource, principalDetails);
    }

    @PutMapping("/api/board/step/3")
    public Long updateBoardStepRecipe(@RequestPart BoardRequestDtoStepRecipe boardRequestDtoStepRecipe,
                                      @RequestPart MultipartFile multipartFile,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.updateBoardStepRecipe(boardRequestDtoStepRecipe, multipartFile,principalDetails);
    }

    @DeleteMapping("/api/board/step/3")
    public void deleteRecipeStep(@RequestPart Long boardId,
                                 @RequestPart Integer stepNum,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails){

        boardService.deleteRecipeStep(boardId,stepNum,principalDetails);
    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @PathVariable Long boardId) {

        boardService.deleteBoard(principalDetails, boardId);
    }


}