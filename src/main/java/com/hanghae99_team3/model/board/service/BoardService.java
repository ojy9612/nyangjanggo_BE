package com.hanghae99_team3.model.board.service;


import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepMain;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepRecipe;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepResource;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.service.ResourceSearchService;
import com.hanghae99_team3.model.resource.service.ResourceService;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;
import static com.hanghae99_team3.exception.ErrorMessage.ID_DUPLICATE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final AwsS3Service awsS3Service;
    private final ResourceService resourceService;
    private final ResourceSearchService resourceSearchService;
    private final RecipeStepService recipeStepService;


    public Board findBoardById (Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND)
        );
    }

    public Board getOneBoard(Long boardId) {
        return this.findBoardById(boardId);
    }

    public Page<Board> getAllBoards(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }


    public Page<Board> getByResource(String searchWord,Pageable pageable) {

        Set<Long> boardIdSet = resourceSearchService.getByResourceName(searchWord);

        return boardRepository.findAllByIdIn(boardIdSet,pageable);
    }

    public Optional<Board> createBoardStepStart(PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        return boardRepository.findByUserAndStatusStartsWith(user,"step");
    }

    @Transactional
    public Long createBoardStepMain(BoardRequestDtoStepMain boardRequestDtoStepMain, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = Board.builder()
                .user(user)
                .boardRequestDtoStepMain(boardRequestDtoStepMain)
                .mainImage(awsS3Service.uploadFile(multipartFile))
                .status("step 1")
                .build();

        boardRepository.save(board);

        return board.getId();
    }

    @Transactional
    public Long createBoardStepResource(BoardRequestDtoStepResource boardRequestDtoStepResource, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardRequestDtoStepResource.getBoardId());
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        resourceService.createResource(boardRequestDtoStepResource.getResourceRequestDtoList(),board);

        board.setStatus("step 2");

        return board.getId();
    }

    @Transactional
    public Long createBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardRequestDtoStepRecipe.getBoardId());
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        recipeStepService.createRecipeStep(boardRequestDtoStepRecipe.getRecipeStepRequestDto(),multipartFile, board);

        board.setStatus("step 3");

        return board.getId();
    }

    @Transactional
    public void createBoardStepEnd(Long boardId, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        board.setStatus("complete");
    }


    @Transactional
    public Long updateBoardStepMain(Long boardId,BoardRequestDtoStepMain boardRequestDtoStepMain, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        if (Objects.equals(multipartFile.getOriginalFilename(), "")){
            awsS3Service.deleteFile(board.getMainImage());
            board.updateStepMain(boardRequestDtoStepMain,awsS3Service.uploadFile(multipartFile));
            return board.getId();
        }

        board.updateStepMain(boardRequestDtoStepMain,board.getMainImage());
        return board.getId();
    }

    @Transactional
    public Long updateBoardStepResource(BoardRequestDtoStepResource boardRequestDtoStepResource, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardRequestDtoStepResource.getBoardId());
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        resourceService.updateResource(boardRequestDtoStepResource.getResourceRequestDtoList(),board);

        return board.getId();
    }

    @Transactional
    public Long updateBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardRequestDtoStepRecipe.getBoardId());
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        recipeStepService.updateStep(board,boardRequestDtoStepRecipe.getRecipeStepRequestDto(),multipartFile);

        return board.getId();
    }

    public void deleteRecipeStep(Long boardId, Integer stepNum, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        recipeStepService.removeAndResortRecipeStep(board,stepNum);
    }

    public void deleteBoard(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        boardRepository.delete(board);
    }

}
