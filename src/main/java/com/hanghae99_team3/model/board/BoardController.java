package com.hanghae99_team3.model.board;


import com.hanghae99_team3.config.CacheKey;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.response.BoardDetailResponseDto;
import com.hanghae99_team3.model.board.dto.response.BoardResponseDto;
import com.hanghae99_team3.model.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Cacheable(value = CacheKey.BOARD, key = "#boardId", cacheManager = "cacheManager")
    @GetMapping("/api/board/{boardId}")
    public BoardDetailResponseDto getOneBoard(@PathVariable Long boardId) {
        return new BoardDetailResponseDto(boardService.findBoardById(boardId));
    }

    @GetMapping("/api/boards/user/good")
    public Page<BoardResponseDto> getBoardByUserGood(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     Pageable pageable) {
        return boardService.getBoardByUserGood(principalDetails, pageable).map(BoardResponseDto::new);
    }

    @GetMapping("/api/boards/preview")
    public List<BoardResponseDto> getBoardsBySortPreview(@RequestParam String entityName) {
        return boardService.getBoardsBySortPreview(entityName).stream()
                .map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping("/api/boards")
    public Page<BoardResponseDto> getAllBoards(Pageable pageable) {
        return boardService.getAllBoards(pageable).map(BoardResponseDto::new);
    }

    @PostMapping("/api/board/image")
    public Map<String, String> createImage(@RequestPart MultipartFile multipartFile,
                                           @RequestParam Long boardId) {

        Map<String, String> result = new HashMap<>();
        result.put("imageLink",
                boardService.createImage(multipartFile, boardId)
        );
        return result;
    }

    @GetMapping("/api/board/check")
    public BoardDetailResponseDto checkModifyingBoard(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return new BoardDetailResponseDto(
                boardService.checkModifyingBoard(principalDetails)
        );
    }

    @PutMapping("/api/board/temp")
    public void createTempBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @RequestParam Long boardId,
                                @RequestPart BoardRequestDto boardRequestDto) {

        boardService.createTempBoard(principalDetails, boardId, boardRequestDto);
    }

    @PostMapping("/api/board")
    public void createBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @RequestParam Long boardId,
                            @RequestPart BoardRequestDto boardRequestDto) {

        boardService.createBoard(principalDetails, boardId, boardRequestDto);
    }

    @PutMapping("/api/board")
    public void updateBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @RequestParam Long boardId,
                            @RequestPart BoardRequestDto boardRequestDto) {

        boardService.updateBoard(principalDetails, boardId, boardRequestDto);
    }

    @DeleteMapping("/api/board")
    public void deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @RequestParam Long boardId) {

        boardService.deleteBoard(principalDetails, boardId);
    }

}