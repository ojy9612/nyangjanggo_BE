package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.board.BoardRepository;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepositpory goodRepositpory;
    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    public void createAndRemoveGood(PrincipalDetails principalDetails, Long boardId) {
        User user = userRepository.findByEmail(principalDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        Optional<Good> optionalGood = goodRepositpory.findByBoardAndUser(board,user);

        if (optionalGood.isPresent()){
            goodRepositpory.delete(optionalGood.get());
        }else{
            Good good = Good.builder().board(board).user(user).build();
            goodRepositpory.save(good);
        }

    }
}
