package com.hanghae99_team3.model.good;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.config.SaveCount;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepositpory goodRepositpory;
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final SaveCount saveCount;

    public void createAndRemoveGood(PrincipalDetails principalDetails, Long boardId) {
        User user = userService.findUserByAuthEmail(principalDetails);
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException(BOARD_NOT_FOUND));

        Optional<Good> optionalGood = goodRepositpory.findByBoardAndUser(board, user);

        if (optionalGood.isPresent()) {
            goodRepositpory.delete(optionalGood.get());
        } else {
            Good good = Good.builder().board(board).user(user).build();
            goodRepositpory.save(good);
        }
        saveCount.appendBoardId(board.getId());
    }

    public List<Long> getBoardIdListByUser(User user) {
        return goodRepositpory.findAllByUser(user).stream()
                .map(good -> good.getBoard().getId()).collect(Collectors.toList());
    }

}
