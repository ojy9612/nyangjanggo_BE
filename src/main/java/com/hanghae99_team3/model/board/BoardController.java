package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.board.dto.*;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/board/{boardId}")
    public BoardDetailResponseDto getOneBoard(@PathVariable Long boardId) {
        return new BoardDetailResponseDto(boardService.getOneBoard(boardId));
    }

    @GetMapping("/api/boards")
    public Page<BoardResponseDto> getAllBoards(@PageableDefault(size = 3) Pageable pageable) {
        return boardService.getAllBoards(pageable);
    }


    @GetMapping(value = "/api/board/step/0")
    public BoardDetailResponseDto createBoardStepStart(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return boardService.createBoardStepStart(principalDetails)
                .map(BoardDetailResponseDto::new).orElse(null);

    }

    @PostMapping(value = "/api/board/step/1")
    public Map<String, Long> createBoardStepMain(@RequestPart BoardRequestDtoStepMain boardRequestDtoStepMain,
                                                 @RequestPart MultipartFile multipartFile,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Map<String, Long> result = new HashMap<>();
        result.put("boardId",
                boardService.createBoardStepMain(boardRequestDtoStepMain, multipartFile, principalDetails)
        );
        return result;

    }

    @PostMapping(value = "/api/board/step/2")
    public Map<String, Long> createBoardStepResource(@RequestPart BoardRequestDtoStepResource boardRequestDtoStepResource,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Map<String, Long> result = new HashMap<>();
        result.put("boardId",
                boardService.createBoardStepResource(boardRequestDtoStepResource, principalDetails)
        );
        return result;

    }

    @PostMapping(value = "/api/board/step/3")
    public Map<String, Long> createBoardStepRecipe(@RequestPart BoardRequestDtoStepRecipe boardRequestDtoStepRecipe,
                                                   @RequestPart MultipartFile multipartFile,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Map<String, Long> result = new HashMap<>();
        result.put("boardId",
                boardService.createBoardStepRecipe(boardRequestDtoStepRecipe, multipartFile, principalDetails)
        );
        return result;

    }

    @PostMapping(value = "/api/board/step/-1")
    public void createBoardStepEnd(@RequestPart Long boardId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.createBoardStepEnd(boardId, principalDetails);

    }

    @PutMapping("/api/board/step/1")
    public Map<String, Long> updateBoardStepMain(@RequestPart Long boardId,
                                                 @RequestPart BoardRequestDtoStepMain boardRequestDtoStepMain,
                                                 @RequestPart MultipartFile multipartFile,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Map<String, Long> result = new HashMap<>();
        result.put("boardId",
                boardService.updateBoardStepMain(boardId, boardRequestDtoStepMain, multipartFile, principalDetails)
        );
        return result;

    }

    @PutMapping("/api/board/step/2")
    public Map<String, Long> updateBoardStepResource(@RequestPart BoardRequestDtoStepResource boardRequestDtoStepResource,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Map<String, Long> result = new HashMap<>();
        result.put("boardId",
                boardService.updateBoardStepResource(boardRequestDtoStepResource, principalDetails)
        );
        return result;
    }

    @PutMapping("/api/board/step/3")
    public Map<String, Long> updateBoardStepRecipe(@RequestPart BoardRequestDtoStepRecipe boardRequestDtoStepRecipe,
                                                   @RequestPart MultipartFile multipartFile,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Map<String, Long> result = new HashMap<>();
        result.put("boardId",
                boardService.updateBoardStepRecipe(boardRequestDtoStepRecipe, multipartFile, principalDetails)
        );
        return result;
    }

    @DeleteMapping("/api/board/step/3")
    public void deleteRecipeStep(@RequestPart Long boardId,
                                 @RequestPart Integer stepNum,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.deleteRecipeStep(boardId, stepNum, principalDetails);
    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @PathVariable Long boardId) {

        boardService.deleteBoard(principalDetails, boardId);
    }

    @PostMapping("/test")
    public Map<String, String> test(@RequestPart MultipartFile multipartFile){

        Map<String, String> testmap = new HashMap<>();

        testmap.put("contentType",multipartFile.getContentType());
        testmap.put("OriginalFilename",multipartFile.getOriginalFilename());
        testmap.put("Name",multipartFile.getName());
        testmap.put("비교!", (multipartFile.getContentType() == null ? "null이 맞네요" : "null이 아니네요 ㅠ") );

        return testmap;
    }

}