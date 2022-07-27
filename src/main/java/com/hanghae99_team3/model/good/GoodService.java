package com.hanghae99_team3.model.good;

import com.hanghae99_team3.model.board.config.SaveCount;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepositpory goodRepositpory;
    private final BoardService boardService;
    private final UserService userService;
    private final SaveCount saveCount;

    public void createAndRemoveGood(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = boardService.findBoardById(boardId);

        Optional<Good> optionalGood = goodRepositpory.findByBoardAndUser(board,user);

        if (optionalGood.isPresent()){
            goodRepositpory.delete(optionalGood.get());
        }else{
            Good good = Good.builder().board(board).user(user).build();
            goodRepositpory.save(good);
        }
        saveCount.appendBoardId(board.getId());

    }
}
