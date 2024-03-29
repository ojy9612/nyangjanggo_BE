package com.hanghae99_team3.model.board;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.domain.BoardDocument;
import com.hanghae99_team3.model.board.dto.response.BoardResponseDto;
import com.hanghae99_team3.model.board.repository.BoardDocumentRepository;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.domain.Resource;
import com.hanghae99_team3.model.resource.domain.ResourceKeywordDocument;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.resource.repository.ResourceKeywordDocumentRepository;
import com.hanghae99_team3.model.resource.service.ResourceService;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final BoardDocumentRepository boardDocumentRepository;
    private final BoardRepository boardRepository;
    private final ResourceKeywordDocumentRepository resourceKeywordDocumentRepository;
    private final UserService userService;
    private final ResourceService resourceService;
    private final RecipeStepService recipeStepService;


    @GetMapping("/api/boards/elastic")
    public Page<BoardResponseDto> getAllBoardDocument(Pageable pageable){
        List<Long> boardIdList = boardDocumentRepository.findFirst2By().stream()
                .map(BoardDocument::getId).collect(Collectors.toList());

        return boardRepository.findAllByIdInAndStatus(boardIdList, pageable, "complete").map(BoardResponseDto::new);
    }
    @GetMapping("/api/resources/elastic")
    public List<ResourceKeywordDocument> getAllResourceKeywordDocument(){

        return resourceKeywordDocumentRepository.findAll();
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

        resourceKeywordDocumentRepository.saveAll(resourceKeywordDocumentList);
    }


    @PostMapping("/test/boards111")
    @Transactional
    public void createManyBoards(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 @RequestBody TestBoardDtoList testBoardDtoList){
        User user = userService.findUserByAuthEmail(principalDetails);

        testBoardDtoList.getBoardRequestDtoList().forEach(boardRequestDto -> {
            Board board = Board.emptyBuilder()
                    .user(user)
                    .build();

            board.updateStepMain(boardRequestDto);
            resourceService.createResource(boardRequestDto.getResourceRequestDtoList(), board);
            recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

            board.setStatus("complete");
            boardRepository.save(board);
        });

    }

    @PostMapping("/test/board/doc")
    public void asdas(){
        List<Board> all = boardRepository.findAll();
        List<BoardDocument> collect = all.stream()
                .map(BoardDocument::new).collect(Collectors.toList());

        boardDocumentRepository.saveAll(collect);
    }

    @GetMapping("/test/boards/elastic")
    public Page<BoardDocument> findAllBoardDocument(Pageable pageable){
        return boardDocumentRepository.findAll(pageable);
    }

    @GetMapping("/test/resources/elastic")
    public Page<ResourceKeywordDocument> findAllResourceKeyword(Pageable pageable){
        return resourceKeywordDocumentRepository.findAll(pageable);
    }

    @DeleteMapping("/test/boards/elastic")
    public void deleteBoardDocuments(){
        boardDocumentRepository.deleteAll();
    }
}

