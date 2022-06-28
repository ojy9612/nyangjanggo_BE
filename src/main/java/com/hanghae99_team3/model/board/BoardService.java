package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.member.UserRepository;
import com.hanghae99_team3.model.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public Board getOneBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 계정이 존재하지 않습니다.")
        );
    }


    public List<Board> getAllBoards(){

        return boardRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));


    }
    @Transactional
    public Board createBoard(@RequestBody BoardRequestDto boardRequestDto, User userDetails){
        User longinUser = userRepository.findById(userDetails.getId()).orElseThrow(
                ()-> new IllegalArgumentException("현재 로그인 되어 있지 않습니다")
        );
        Board board = Board.builder()
                .user(longinUser)
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .build();

        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(BoardRequestDto boardRequestDto, User user, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );
        board.update(boardRequestDto);
        return board;
    }


    @Transactional
    public void deleteBoard(User user, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );
        boardRepository.delete(board);
    }
}
