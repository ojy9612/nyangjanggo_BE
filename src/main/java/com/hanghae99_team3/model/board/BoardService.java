package com.hanghae99_team3.model.board;


import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.model.board.dto.BoardRequestDtoStepMain;
import com.hanghae99_team3.model.board.dto.BoardRequestDtoStepRecipe;
import com.hanghae99_team3.model.board.dto.BoardRequestDtoStepResource;
import com.hanghae99_team3.model.board.dto.BoardResponseDto;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.ResourceService;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    private final RecipeStepService recipeStepService;


    public Board findBoardById (Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND)
        );
    }

    public Board getOneBoard(Long boardId) {
        return this.findBoardById(boardId);
    }

    public Page<BoardResponseDto> getAllBoards(Pageable pageable) {


        Page<Board> all = boardRepository.findAll(pageable);
        List<Board> content1 = all.getContent();
        Page<BoardResponseDto> map1 = boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);
        List<BoardResponseDto> content = map1.getContent();

        return map1;
    }


    public Optional<Board> createBoardStepStart(PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        return boardRepository.findByUserAndStatusStartsWith(user,"step");
    }

    @Transactional
    public void createBoardStepMain(BoardRequestDtoStepMain boardRequestDtoStepMain, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = Board.builder()
                .user(user)
                .boardRequestDtoStepMain(boardRequestDtoStepMain)
                .mainImage(awsS3Service.uploadFile(multipartFile))
                .status("step 1")
                .build();

        boardRepository.save(board);
    }

    @Transactional
    public void createBoardStepResource(BoardRequestDtoStepResource boardRequestDtoStepResource, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = boardRepository.findById(boardRequestDtoStepResource.getBoardId()).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND)
        );

        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        resourceService.createResource(boardRequestDtoStepResource.getResourceRequestDtoList(),board);

        board.setStatus("step 2");
    }

    @Transactional
    public void createBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = boardRepository.findById(boardRequestDtoStepRecipe.getBoardId()).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND)
        );

        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        recipeStepService.createRecipeStep(boardRequestDtoStepRecipe.getRecipeStepRequestDto(), awsS3Service.uploadFile(multipartFile), board);
    }

    @Transactional
    public void createBoardStepEnd(Long boardId, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND)
        );

        if (user != board.getUser()) throw new IdDuplicateException(ID_DUPLICATE);

        board.setStatus("complete");
    }

//    @Transactional
//    public Board updateBoard(BoardRequestDtoStep0 boardRequestDtoStepZero, PrincipalDetails principalDetails, Long boardId) {
//        User user = userService.findUserByAuthEmail(principalDetails);
//
//        Board board = boardRepository.findById(boardId).orElseThrow(
//                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
//        );
//
//        if (user.getId().equals(board.getUser().getId())){
//
//            recipeStepService.removeRecipeStep(board);
//            recipeStepService.createRecipeStep(boardRequestDtoStepZero.getRecipeStepRequestDtoList(),board);
//
//            resourceService.removeResource(board);
//            resourceService.createResource(boardRequestDtoStepZero.getResourceRequestDtoList(),board);
//
//            imagesService.removeImages(board);
//            imagesService.createImages(boardRequestDtoStepZero.getImgFileList(), board);
//
//            awsS3Service.deleteFile(board.getMainImage());
//            board.update(boardRequestDtoStepZero, awsS3Service.uploadFile(boardRequestDtoStepZero.getMainImageFile()));
//            return board;
//        } else{
//            throw new IdDuplicateException(ID_DUPLICATE);
//        }
//
//    }


    @Transactional
    public void deleteBoard(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND)
        );

        if (user.getId().equals(board.getUser().getId())){
            boardRepository.delete(board);
        } else{
            throw new IdDuplicateException(ID_DUPLICATE);
        }

    }

}
