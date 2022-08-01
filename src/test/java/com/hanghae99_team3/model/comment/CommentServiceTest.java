package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    BoardService boardService;
    @Mock
    UserService userService;

    @InjectMocks
    CommentService commentService;

    User baseUser;
    PrincipalDetails baseUserDetails;
    Board baseBoard;


    @BeforeEach
    public void setUp() {
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
        baseBoard = Board.emptyBuilder().user(baseUser).build();


    }

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {

        @Test
        @DisplayName("모든 댓글 조회")
        void getAllComment() {

            //given
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                userList.add(
                        User.testRegister()
                                .email("email@test.com" + i)
                                .password("password")
                                .userImg("userImgLink")
                                .provider(AuthProvider.kakao)
                                .providerId("providerId")
                                .nickname("nickname")
                                .role(UserRole.USER)
                                .userDescription("description")
                                .build()
                );
            }
            List<CommentRequestDto> commentRequestDtoList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                commentRequestDtoList.add(
                        CommentRequestDto.builder()
                                .content("내용" + i)
                                .build()
                );
            }
            List<Comment> commentList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                commentList.add(
                        Comment.builder()
                                .commentRequestDto(commentRequestDtoList.get(i))
                                .board(baseBoard)
                                .user(userList.get(i))
                                .build()
                );
            }
            Page<Comment> commentPage = new PageImpl<>(commentList);
            Pageable pageable = PageRequest.of(0, 5);

            //when
            when(boardService.findBoardById(
                    anyLong()
            )).thenReturn(baseBoard);
            when(commentRepository.findAllByBoard(
                    any(Board.class),
                    any(Pageable.class)
            )).thenReturn(commentPage);
            Page<CommentResponseDto> result = commentService.getAllComment(1L, pageable);

            //then
            assertThat(result.getContent().get(5).getComment()).isEqualTo("내용5");

        }


        @Test
        @DisplayName("댓글 생성")
        void createComment() {
            //given
            CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                    .content("내용")
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);
            when(boardService.findBoardById(
                    anyLong()
            )).thenReturn(baseBoard);


            commentService.createComment(baseUserDetails, commentRequestDto, 1L);
            //then
            assertThat(baseBoard.getCommentList().get(0).getContent()).isEqualTo("내용");
        }

    }
}




