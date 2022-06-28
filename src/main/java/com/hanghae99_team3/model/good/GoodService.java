package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.board.BoardRepository;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepositpory goodRepositpory;
    private final BoardRepository boardRepository;

    public void createAndRemoveGood(User userDetails, Long boardId) {
        User user = userDetails;
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
