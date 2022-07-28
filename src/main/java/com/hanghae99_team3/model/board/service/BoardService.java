package com.hanghae99_team3.model.board.service;


import com.hanghae99_team3.exception.newException.IdDifferentException;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.good.GoodService;
import com.hanghae99_team3.model.images.ImagesService;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.service.ResourceService;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;
import static com.hanghae99_team3.exception.ErrorMessage.USER_ID_DIFFERENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardDocumentService boardDocumentService;
    private final UserService userService;
    private final GoodService goodService;
    private final ImagesService imagesService;
    private final ResourceService resourceService;
    private final RecipeStepService recipeStepService;


    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));
    }

    // 게시글 10개만 보여주기 (colum 이름 기준)
    public List<Board> getBoardsBySortPreview(String columName) {
        return boardRepository.findFirst10By(Sort.by(Sort.Direction.DESC, columName));
    }

    // 전체 게시글 조회 ex) [url]?page=0&size=10&sort=goodCount,desc
    public Page<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 좋아요 누른 게시글만 보여주기
    public Page<Board> getBoardByUserGood(PrincipalDetails principalDetails, Pageable pageable) {
        User user = userService.findUserByAuthEmail(principalDetails);
        List<Long> boardIdList = goodService.getBoardIdListByUser(user);

        return boardRepository.findAllByIdIn(boardIdList, pageable);
    }

    // 이미지만 업로드하기
    @Transactional
    public String createImage(MultipartFile multipartFile, Long boardId) {
        Board board = this.findBoardById(boardId);

        return imagesService.createImages(multipartFile, board);
    }

    // 작성중인 게시글이 있는지 확인 없을시 빈 게시글을 생성해서 반환 함.
    @Transactional
    public Board checkModifyingBoard(PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Optional<Board> optionalBoard = boardRepository.findByUserAndStatus(user, "modifying");

        if (optionalBoard.isPresent()) {
            return optionalBoard.get();
        } else {
            Board board = Board.emptyBuilder().user(user).build();
            return boardRepository.save(board);
        }
    }

    // 게시글 임시 저장 (작성중인 게시글 등록)
    @Transactional
    public void createTempBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        Board board = this.findBoardById(boardId);
        if (!principalDetails.getUserId().equals(board.getUser().getId()))
            throw new IdDifferentException(USER_ID_DIFFERENT);

        board.updateStepMain(boardRequestDto);
        resourceService.updateResource(boardRequestDto.getResourceRequestDtoList(), board);
        recipeStepService.updateRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(), board);
    }

    @Transactional
    public void createBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        Board board = this.findBoardById(boardId);
        if (!principalDetails.getUserId().equals(board.getUser().getId()))
            throw new IdDifferentException(USER_ID_DIFFERENT);

        board.updateStepMain(boardRequestDto);
        resourceService.createResource(boardRequestDto.getResourceRequestDtoList(), board);
        recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(), board);

        boardDocumentService.createBoard(board);
        board.setStatus("complete");
    }

    @Transactional
    public void updateBoard(PrincipalDetails principalDetails, Long boardId, BoardRequestDto boardRequestDto) {
        Board board = this.findBoardById(boardId);
        if (!principalDetails.getUserId().equals(board.getUser().getId()))
            throw new IdDifferentException(USER_ID_DIFFERENT);

        board.updateStepMain(boardRequestDto);
        resourceService.updateResource(boardRequestDto.getResourceRequestDtoList(), board);
        recipeStepService.updateRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(), board);

        boardDocumentService.updateBoard(board);
    }

    public void deleteBoard(PrincipalDetails principalDetails, Long boardId) {
        Board board = this.findBoardById(boardId);
        if (!principalDetails.getUserId().equals(board.getUser().getId()))
            throw new IdDifferentException(USER_ID_DIFFERENT);

        boardDocumentService.deleteBoard(board);
        boardRepository.delete(board);
    }

}
