package com.hanghae99_team3.model.board;


import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepMain;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepRecipe;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepResource;
import com.hanghae99_team3.model.board.dto.response.BoardDetailResponseDto;
import com.hanghae99_team3.model.board.dto.response.BoardResponseDto;
import com.hanghae99_team3.model.board.service.BoardDocumentService;
import com.hanghae99_team3.model.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardDocumentService boardDocumentService;

    @GetMapping("/api/board/{boardId}")
    public BoardDetailResponseDto getOneBoard(@PathVariable Long boardId) {
        return new BoardDetailResponseDto(boardService.getOneBoard(boardId));
    }

    @GetMapping("/api/board/resource/recommend")
    public List<String> recommendResource(@RequestParam String resourceName){
        return boardDocumentService.recommendResource(resourceName);
    }

    @GetMapping("/api/boards/resource")
    public Page<BoardResponseDto> getBoardDocumentsByResource(@RequestParam String resourceNameWords,
                                                              Pageable pageable){
        return boardDocumentService.getBoardDocumentsByResource(resourceNameWords,pageable).map(BoardResponseDto::new);
    }

    @GetMapping("/api/board/title/recommend")
    public Set<String> recommendBoardDocumentByTitle(@RequestParam String titleWords){
        return boardDocumentService.recommendBoardDocumentByTitle(titleWords);
    }

    @GetMapping("/api/boards/title")
    public Page<BoardResponseDto> getBoardDocumentsByTitle(@RequestParam String titleWords,
                                                           Pageable pageable){
        return boardDocumentService.getBoardDocumentsByTitle(titleWords,pageable).map(BoardResponseDto::new);
    }

    @GetMapping("/api/boards")
    public Page<BoardResponseDto> getAllBoards(@PageableDefault(size = 3) Pageable pageable) {
        return boardService.getAllBoards(pageable).map(BoardResponseDto::new);
    }

    @GetMapping("/api/boards/elastic")
    public Page<BoardResponseDto> getAllBoardDocument(Pageable pageable){
        return boardDocumentService.getAllBoardDocument(pageable)
                .map(BoardResponseDto::new);
    }


//    @GetMapping("/api/boards/resource")
//    public Page<BoardResponseDto> getByResource(@RequestParam String searchWord,
//                                                Pageable pageable
//                                                ){
//        return boardService.getByResource(searchWord,pageable).map(BoardResponseDto::new);
//    }

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

}