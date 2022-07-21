package com.hanghae99_team3.model.board.service;


import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.images.ImagesService;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.service.ResourceService;
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
    private final BoardDocumentService boardDocumentService;
    private final UserService userService;
    private final ImagesService imagesService;
    private final ResourceService resourceService;
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

        return boardRepository.queryFirst5By(pageable);
    }


    @Transactional
    public String createImage(MultipartFile multipartFile, Long boardId) {
        Board board = this.findBoardById(boardId);

        return imagesService.createImages(multipartFile,board);
    }

    @Transactional
    public Board checkModifyingBoard(PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Optional<Board> optionalBoard = boardRepository.findByUserAndStatus(user, "modifying");

        if (optionalBoard.isPresent()){
            return optionalBoard.get();
        }else{
            Board board = Board.emptyBuilder().user(user).build();
            board.setStatus("modifying");
            return boardRepository.save(board);
        }
    }


    @Transactional
    public void createBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        board.updateStepMain(boardRequestDto);
        resourceService.createResource(boardRequestDto.getResourceRequestDtoList(),board);
        recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        boardDocumentService.createBoard(board);
        board.setStatus("complete");
    }

    @Transactional
    public void createTempBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        board.updateStepMain(boardRequestDto);
        resourceService.updateResource(boardRequestDto.getResourceRequestDtoList(),board);
        recipeStepService.updateRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        board.setStatus("modifying");
    }

    @Transactional
    public void updateBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        board.updateStepMain(boardRequestDto);
        resourceService.updateResource(boardRequestDto.getResourceRequestDtoList(),board);
        recipeStepService.updateRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        boardDocumentService.updateBoard(board);
        board.setStatus("complete");
    }

    public void deleteBoard(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        boardDocumentService.deleteBoard(board);
        boardRepository.delete(board);
    }

//    @Transactional
//    public Long createBoardStepMain(BoardRequestDtoStepMain boardRequestDtoStepMain, MultipartFile multipartFile, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//
//        Board board = Board.builder()
//                .user(user)
//                .boardRequestDtoStepMain(boardRequestDtoStepMain)
//                .mainImage(awsS3Service.uploadFile(multipartFile))
//                .status("step 1")
//                .build();
//
//        boardRepository.save(board);
//
//        return board.getId();
//    }
//
//    @Transactional
//    public Long createBoardStepResource(BoardRequestDtoStepResource boardRequestDtoStepResource, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardRequestDtoStepResource.getBoardId());
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        resourceService.createResource(boardRequestDtoStepResource.getResourceRequestDtoList(),board);
//
//        board.setStatus("step 2");
//
//        return board.getId();
//    }
//
//    @Transactional
//    public Long createBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe, MultipartFile multipartFile, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardRequestDtoStepRecipe.getBoardId());
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        recipeStepService.createRecipeStep(boardRequestDtoStepRecipe.getRecipeStepRequestDto(),multipartFile, board);
//
//        board.setStatus("step 3");
//
//        return board.getId();
//    }
//
//    @Transactional
//    public void createBoardStepEnd(Long boardId, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardId);
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        board.setStatus("complete");
//
//        boardDocumentService.createBoard(board);
//    }
//
//
//    @Transactional
//    public Long updateBoardStepMain(Long boardId,BoardRequestDtoStepMain boardRequestDtoStepMain, MultipartFile multipartFile, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardId);
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        if (Objects.equals(multipartFile.getOriginalFilename(), "")){
//            awsS3Service.deleteFile(board.getMainImageLink());
//            board.updateStepMain(boardRequestDtoStepMain,awsS3Service.uploadFile(multipartFile));
//            return board.getId();
//        }
//
//        board.updateStepMain(boardRequestDtoStepMain,board.getMainImageLink());
//        return board.getId();
//    }
//
//    @Transactional
//    public Long updateBoardStepResource(BoardRequestDtoStepResource boardRequestDtoStepResource, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardRequestDtoStepResource.getBoardId());
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        resourceService.updateResource(boardRequestDtoStepResource.getResourceRequestDtoList(),board);
//
//        return board.getId();
//    }
//
//    @Transactional
//    public Long updateBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe, MultipartFile multipartFile, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardRequestDtoStepRecipe.getBoardId());
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        recipeStepService.updateStep(board,boardRequestDtoStepRecipe.getRecipeStepRequestDto(),multipartFile);
//
//        return board.getId();
//    }
//
//    public void deleteRecipeStep(Long boardId, Integer stepNum, PrincipalDetails principalDetails) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//        Board board = this.findBoardById(boardId);
//        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);
//
//        recipeStepService.removeAndResortRecipeStep(board,stepNum);
//    }


}
