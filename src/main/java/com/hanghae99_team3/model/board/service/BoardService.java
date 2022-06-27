package com.hanghae99_team3.model.board.service;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.BoardRequestDto;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.member.domain.Member;
import com.hanghae99_team3.model.member.repository.UserRepository;
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
    public Board createBoard(@RequestBody BoardRequestDto boardRequestDto, Member memberDetails){
        boardRequestDto.setUserId(memberDetails.getId());
        Member longinMember = userRepository.findById(boardRequestDto.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("현재 로그인 되어 있지 않습니다")
        );
        Board board = Board.builder()
                .member(longinMember)
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .imgLink(boardRequestDto.getImgLink())
                .imgKey(boardRequestDto.getImgKey())
                .build();

        return boardRepository.save(board);

    }

    @Transactional
    public Board updateBoard(BoardRequestDto boardRequestDto, Member member, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );
        board.update(boardRequestDto);
        return board;
    }


    @Transactional
    public void deleteBoard(Member member, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new IllegalArgumentException("생성된 게시글이 없습니다.")
        );
        boardRepository.delete(board);
    }
}
