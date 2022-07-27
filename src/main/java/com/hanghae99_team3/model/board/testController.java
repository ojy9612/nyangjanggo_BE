package com.hanghae99_team3.model.board;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.response.BoardResponseDto;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.repository.BoardDocumentRepository;
import com.hanghae99_team3.model.board.service.BoardDocumentService;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.resource.repository.ResourceSearchRepository;
import com.hanghae99_team3.model.resource.service.ResourceService;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class testController {

    private final BoardDocumentRepository boardDocumentRepository;
    private final BoardRepository boardRepository;
    private final BoardDocumentService boardDocumentService;
    private final ResourceSearchRepository resourceSearchRepository;
    private final UserService userService;
    private final ResourceService resourceService;
    private final RecipeStepService recipeStepService;
    private final ServletWebServerApplicationContext webServerAppCtxt;


    @GetMapping("/api/boards/elastic")
    public Page<BoardResponseDto> getAllBoardDocument(Pageable pageable){
        List<Long> boardIdList = boardDocumentRepository.findFirst2By().stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdIn(boardIdList, pageable).map(BoardResponseDto::new);
    }
    @GetMapping("/api/resources/elastic")
    public List<ResourceKeywordDocument> getAllResourceKeywordDocument(Pageable pageable){

        return resourceSearchRepository.findAll();
    }

    @PostMapping("/test/resources")
    public void createManyResources(@RequestBody TestWrapper resources){
        List<ResourceKeywordDocument> resourceKeywordDocumentList = new ArrayList<>();

        resources.getResources().forEach(resource -> {
            Resource resource1 = new Resource (
                    ResourceRequestDto.builder().resourceName(resource).amount("1").category("카테고리").build()
            );

            ResourceKeywordDocument resourceKeywordDocument = ResourceKeywordDocument.builder()
                    .resource(resource1)
                    .build();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocument.plusCnt();
            resourceKeywordDocumentList.add(resourceKeywordDocument);
        });

        resourceSearchRepository.saveAll(resourceKeywordDocumentList);
    }

    @PostMapping("/test/board/bad")
    @Transactional
    public void createBoardTest(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @RequestPart BoardRequestDto boardRequestDto){
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = Board.builder()
                .boardRequestDto(boardRequestDto)
                .user(user)
                .build();

        resourceService.createResourceTest(boardRequestDto.getResourceRequestDtoList(), board);
        recipeStepService.createRecipeStepTest(boardRequestDto.getRecipeStepRequestDtoList(),board);

        board.setStatus("complete");
        boardDocumentService.createBoard(board);
        boardRepository.save(board);
    }

    @PostMapping("/test/board/good")
    @Transactional
    public void createBoardTest2(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @RequestPart BoardRequestDto boardRequestDto){
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = Board.builder()
                .boardRequestDto(boardRequestDto)
                .user(user)
                .build();

        resourceService.createResource(boardRequestDto.getResourceRequestDtoList(), board);
        recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        board.setStatus("complete");
        boardDocumentService.createBoard(board);
        boardRepository.save(board);
    }


    @PostMapping("/test/boards111")
    @Transactional
    public void createManyBoards(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 @RequestBody TestBoardDtoList testBoardDtoList){
        User user = userService.findUserByAuthEmail(principalDetails);

        List<Board> boardList = new ArrayList<>();

        testBoardDtoList.getBoardRequestDtoList().forEach(boardRequestDto -> {
            Board board = Board.builder()
                    .boardRequestDto(boardRequestDto)
                    .user(user)
                    .build();

            resourceService.createResource(boardRequestDto.getResourceRequestDtoList(), board);
            recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

            board.setStatus("complete");
            boardDocumentService.createBoard(board);
            boardList.add(board);
        });
        boardRepository.saveAll(boardList);

    }

    @PostMapping("/test/board/doc")
    public void asdas(){
        List<Board> all = boardRepository.findAll();
        List<BoardDocument> collect = all.stream()
                .map(BoardDocument::new).collect(Collectors.toList());

        boardDocumentRepository.saveAll(collect);

    }

}

