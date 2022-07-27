package com.hanghae99_team3.model.board.service;


import com.hanghae99_team3.exception.newException.IdDifferentException;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;
import static com.hanghae99_team3.exception.ErrorMessage.USER_ID_DIFFERENT;

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

    public List<Board> getBoardsBySortPreview(String entityName) {
        return boardRepository.findFirst10By(Sort.by(entityName));
    }

    public Page<Board> getAllBoardsBySort(Pageable pageable) {
        return boardRepository.findAll(pageable);
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
            return boardRepository.save(board);
        }
    }

    @Transactional
    public void createTempBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDifferentException(USER_ID_DIFFERENT);

        board.updateStepMain(boardRequestDto);
        resourceService.updateResource(boardRequestDto.getResourceRequestDtoList(),board);
        recipeStepService.updateRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);
    }

    @Transactional
    public void createBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDifferentException(USER_ID_DIFFERENT);

        board.updateStepMain(boardRequestDto);
        resourceService.createResource(boardRequestDto.getResourceRequestDtoList(),board);
        recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        boardDocumentService.createBoard(board);
        board.setStatus("complete");
    }

    @Transactional
    public void updateBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDifferentException(USER_ID_DIFFERENT);

        board.updateStepMain(boardRequestDto);
        resourceService.updateResource(boardRequestDto.getResourceRequestDtoList(),board);
        recipeStepService.updateRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        boardDocumentService.updateBoard(board);
    }

    public void deleteBoard(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = this.findBoardById(boardId);
        if (user != board.getUser()) throw new IdDifferentException(USER_ID_DIFFERENT);

        boardDocumentService.deleteBoard(board);
        boardRepository.delete(board);
    }

    public Page<Board> getAllBoardsByEntityName(String columName, Pageable pageable) {
        return boardRepository.findAllByEntityName(columName,pageable);
    }
}
