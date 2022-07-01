package com.hanghae99_team3.model.board;


import com.hanghae99_team3.exception.newException.IdDuplicateException;
import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.BoardResponseDto;
import com.hanghae99_team3.model.images.ImagesService;
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

import static com.hanghae99_team3.exception.ErrorMessage.ID_DUPLICATE;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final AwsS3Service awsS3Service;
    private final ImagesService imagesService;
    private final ResourceService resourceService;
    private final RecipeStepService recipeStepService;



    public Board getOneBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 계정이 존재하지 않습니다.")
        );
    }



    public Page<BoardResponseDto> getAllBoards(Pageable pageable) {


        Page<Board> all = boardRepository.findAll(pageable);
        List<Board> content1 = all.getContent();
        Page<BoardResponseDto> map1 = boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);
        List<BoardResponseDto> content = map1.getContent();

        return map1;
    }

    @Transactional
    public Board createBoard(BoardRequestDto boardRequestDto, PrincipalDetails principalDetails) {
        User user = userService.findUserByAuthEmail(principalDetails);


        Board board = Board.builder()
                .user(user)
                .boardRequestDto(boardRequestDto)
                .mainImage(awsS3Service.uploadFile(boardRequestDto.getMainImageFile()))
                .build();

        recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

        resourceService.createResource(boardRequestDto.getResourceRequestDtoList(),board);

        imagesService.createImages(boardRequestDto.getImgFileList(), board);

        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(BoardRequestDto boardRequestDto, PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );

        if (user.getId().equals(board.getUser().getId())){

            recipeStepService.removeRecipeStep(board);
            recipeStepService.createRecipeStep(boardRequestDto.getRecipeStepRequestDtoList(),board);

            resourceService.removeResource(board);
            resourceService.createResource(boardRequestDto.getResourceRequestDtoList(),board);

            imagesService.removeImages(board);
            imagesService.createImages(boardRequestDto.getImgFileList(), board);

            awsS3Service.deleteFile(board.getMainImage());
            board.update(boardRequestDto, awsS3Service.uploadFile(boardRequestDto.getMainImageFile()));
            return board;
        } else{
            throw new IdDuplicateException(ID_DUPLICATE);
        }

    }


    @Transactional
    public void deleteBoard(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );

        if (user.getId().equals(board.getUser().getId())){
            boardRepository.delete(board);
        } else{
            throw new IdDuplicateException(ID_DUPLICATE);
        }

    }

}
