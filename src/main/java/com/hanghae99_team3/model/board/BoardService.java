package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.dto.BoardResponseDto;
import com.hanghae99_team3.model.images.ImagesService;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.UserRepository;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    private final ImagesService imagesService;


    public Board getOneBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 계정이 존재하지 않습니다.")
        );
    }


    public Page<BoardResponseDto> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);


    }

    @Transactional
    public Board createBoard(BoardRequestDto boardRequestDto, PrincipalDetails userDetails) {
        User longinUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다."));

        // img 확인

        Board board = Board.builder()
                .user(longinUser)
                .boardRequestDto(boardRequestDto)
                .build();

//        imagesService.createImages(awsS3Service.uploadFile(boardRequestDto.getImgFileList()), board);

        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(BoardRequestDto boardRequestDto, PrincipalDetails user, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );
        board.update(boardRequestDto);
        return board;
    }


    @Transactional
    public void deleteBoard(PrincipalDetails user, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );
        boardRepository.delete(board);
    }
}
