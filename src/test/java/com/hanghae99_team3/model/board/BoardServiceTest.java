package com.hanghae99_team3.model.board;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.service.BoardDocumentService;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.images.ImagesService;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.resource.service.ResourceService;
import com.hanghae99_team3.model.user.UserService;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("BoardService 테스트")
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @Mock
    BoardDocumentService boardDocumentService;
    @Mock
    UserService userService;
    @Mock
    ImagesService imagesService;
    @Mock
    ResourceService resourceService;
    @Mock
    RecipeStepService recipeStepService;
    @InjectMocks
    BoardService boardService;

    User baseUser;
    PrincipalDetails baseUserDetails;


    @BeforeEach
    public void setUp(){
        baseUser = User.testRegister()
                .email("email@test.com")
                .password("password")
                .userImg("userImgLink")
                .provider(AuthProvider.kakao)
                .providerId("providerId")
                .nickname("nickname")
                .role(UserRole.USER)
                .userDescription("description")
                .build();

        baseUserDetails = new PrincipalDetails(baseUser);
    }

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest{

        @Test
        @DisplayName("ID로 게시글 조회")
        void findBoardById() {
            //given
            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            Board resultBoard = boardService.findBoardById(1L);

            //then
            assertThat(resultBoard.getUser().getEmail()).isEqualTo("email@test.com");

        }

        @Test
        @DisplayName("전체 게시글 조회")
        void getAllBoards() {
            //given
            List<Board> boardList = new ArrayList<>();
            for(int i = 0; i < 5; i ++){
                boardList.add(Board.emptyBuilder()
                        .user(baseUser)
                        .build()
                );
            }

            Page<Board> boardPage = new PageImpl<>(boardList);

            //when

            //then

        }

        @Test
        void createImage() {
        }

        @Test
        void checkModifyingBoard() {
        }

        @Test
        void createBoard() {
        }

        @Test
        void createTempBoard() {
        }

        @Test
        void updateBoard() {
        }

        @Test
        void deleteBoard() {
        }
    }

}