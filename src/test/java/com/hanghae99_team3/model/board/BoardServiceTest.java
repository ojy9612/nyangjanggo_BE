package com.hanghae99_team3.model.board;

import com.hanghae99_team3.exception.newException.IdDifferentException;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.board.service.BoardDocumentService;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.images.ImagesService;
import com.hanghae99_team3.model.recipestep.RecipeStepService;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
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
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hanghae99_team3.exception.ErrorMessage.BOARD_NOT_FOUND;
import static com.hanghae99_team3.exception.ErrorMessage.USER_ID_DIFFERENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


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
    }

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {

        @Test
        @DisplayName("ID로 게시글 조회")
        void findBoardById() {
            //given
            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            boardRepository.save(board);

//            when
            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            Board resultBoard = boardService.findBoardById(1L);

            //then
            assertThat(resultBoard.getUser().getEmail()).isEqualTo("email@test.com");
        }

        @Test
        @DisplayName("게시글 프리뷰")
        void getBoardsBySortPreview() {
            //given
            List<Board> boardList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                boardList.add(Board.emptyBuilder()
                        .user(baseUser)
                        .build()
                );
            }

            //when
            when(boardRepository.findFirst10By(
                    any(Sort.class)
            )).thenReturn(boardList);

            List<Board> resultBoard = boardService.getBoardsBySortPreview("goodCount");

            //then
            assertThat(resultBoard.get(0).getUser().getEmail()).isEqualTo("email@test.com");
        }

        @Test
        @DisplayName("전체 게시글 조회")
        void getAllBoardsBySort() {
            //given
            List<Board> boardList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                boardList.add(Board.emptyBuilder()
                        .user(baseUser)
                        .build()
                );
            }

            Page<Board> boardPage = new PageImpl<>(boardList);
            Pageable pageable = PageRequest.of(0, 1);

            //when
            when(boardRepository.findAll(
                    any(Pageable.class)
            )).thenReturn(boardPage);

            Page<Board> resultPageBoard = boardService.getAllBoards(pageable);

            //then
            assertThat(resultPageBoard.getContent().get(0).getUser().getEmail()).isEqualTo("email@test.com");
        }

        @Test
        @DisplayName("이미지 생성")
        void createImage() {
            //given
            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            MultipartFile multipartFile = new MockMultipartFile(
                    "test", (byte[]) null
            );

            //when
            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            when(imagesService.createImages(
                    any(MultipartFile.class),
                    any(Board.class)
            )).thenReturn("imageLink");

            String resultImageLink = boardService.createImage(multipartFile, 1L);

            //then
            assertThat(resultImageLink).isEqualTo("imageLink");
        }

        @Test
        @DisplayName("작성중인 게시글 확인 - 작성중인 게시글 없을 때")
        void checkModifyingBoard1() {
            //given
            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);

            when(boardRepository.save(
                    any(Board.class)
            )).thenReturn(board);

            when(boardRepository.findByUserAndStatus(
                    any(User.class),
                    anyString()
            )).thenReturn(Optional.empty());

            Board resultBoard = boardService.checkModifyingBoard(baseUserDetails);
            //then
            assertThat(resultBoard).isEqualTo(board);
        }

        @Test
        @DisplayName("작성중인 게시글 확인 - 작성중인 게시글 있을 때")
        void checkModifyingBoard2() {
            //given
            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);

            when(boardRepository.findByUserAndStatus(
                    any(User.class),
                    anyString()
            )).thenReturn(Optional.of(board));

            Board resultBoard = boardService.checkModifyingBoard(baseUserDetails);
            //then
            assertThat(resultBoard).isEqualTo(board);
        }

        @Test
        @DisplayName("작성중인 게시글 생성")
        void createTempBoard() {
            //given
            List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                resourceRequestDtoList.add(ResourceRequestDto.builder()
                        .resourceName("재료 이름")
                        .amount("재료 수량")
                        .category("재료 카테고리")
                        .build()
                );
            }

            List<RecipeStepRequestDto> recipeStepRequestDtoList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                recipeStepRequestDtoList.add(RecipeStepRequestDto.builder()
                        .stepNum(i)
                        .stepContent("레시피 Step 내용")
                        .imageLink("레시피 이미지 Link")
                        .build()
                );
            }

            BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .mainImageLink("이미지 Link")
                    .resourceRequestDtoList(resourceRequestDtoList)
                    .recipeStepRequestDtoList(recipeStepRequestDtoList)
                    .build();

            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);

            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            boardService.createTempBoard(baseUserDetails, 1L, boardRequestDto);

            //then
            assertThat(board.getTitle()).isEqualTo("제목");
            assertThat(board.getContent()).isEqualTo("내용");
            assertThat(board.getMainImageLink()).isEqualTo("이미지 Link");
            assertThat(board.getStatus()).isEqualTo("modifying");
        }

        @Test
        @DisplayName("게시글 생성")
        void createBoard() {
            //given
            List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                resourceRequestDtoList.add(ResourceRequestDto.builder()
                        .resourceName("재료 이름")
                        .amount("재료 수량")
                        .category("재료 카테고리")
                        .build()
                );
            }

            List<RecipeStepRequestDto> recipeStepRequestDtoList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                recipeStepRequestDtoList.add(RecipeStepRequestDto.builder()
                        .stepNum(i)
                        .stepContent("레시피 Step 내용")
                        .imageLink("레시피 이미지 Link")
                        .build()
                );
            }

            BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .mainImageLink("이미지 Link")
                    .resourceRequestDtoList(resourceRequestDtoList)
                    .recipeStepRequestDtoList(recipeStepRequestDtoList)
                    .build();

            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);

            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            boardService.createBoard(baseUserDetails, 1L, boardRequestDto);

            //then
            assertThat(board.getTitle()).isEqualTo("제목");
            assertThat(board.getContent()).isEqualTo("내용");
            assertThat(board.getMainImageLink()).isEqualTo("이미지 Link");
            assertThat(board.getStatus()).isEqualTo("complete");
        }

        @Test
        @DisplayName("게시글 수정")
        void updateBoard() {
            //given
            List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                resourceRequestDtoList.add(ResourceRequestDto.builder()
                        .resourceName("재료 이름")
                        .amount("재료 수량")
                        .category("재료 카테고리")
                        .build()
                );
            }

            List<RecipeStepRequestDto> recipeStepRequestDtoList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                recipeStepRequestDtoList.add(RecipeStepRequestDto.builder()
                        .stepNum(i)
                        .stepContent("레시피 Step 내용")
                        .imageLink("레시피 이미지 Link")
                        .build()
                );
            }

            BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .mainImageLink("이미지 Link")
                    .resourceRequestDtoList(resourceRequestDtoList)
                    .recipeStepRequestDtoList(recipeStepRequestDtoList)
                    .build();

            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);

            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            boardService.updateBoard(baseUserDetails, 1L, boardRequestDto);

            //then
            assertThat(board.getTitle()).isEqualTo("제목");
            assertThat(board.getContent()).isEqualTo("내용");
            assertThat(board.getMainImageLink()).isEqualTo("이미지 Link");
        }

        @Test
        @DisplayName("통합테스트에서 해야할 듯 게시글 삭제")
        void deleteBoard(){
            //given
            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(baseUser);

            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));

            boardService.deleteBoard(baseUserDetails, 1L);

            //then
            verify(boardRepository, times(1)).delete(any());

        }

    }

    @Nested
    @DisplayName("실패 테스트")
    class FailTest{

        @Test
        @DisplayName("존재하지 않는 게시글 접근")
        void findBoardByIdWrongId(){
            //given
            //when
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    boardService.findBoardById(-1L)
            );
            //then
            assertThat(exception.getMessage()).isEqualTo(BOARD_NOT_FOUND);
        }

        @Test
        @DisplayName("게시글 프리뷰 - 잘못된 엔티티 이름 주입")
        void getBoardsBySortPreviewWrongEntityName() {
            //given
            //when
            when(boardRepository.findFirst10By(
                    any(Sort.class)
            )).thenThrow(new MockitoException("PropertyReferenceException 에러 발생"));
            // PropertyReferenceException

            Exception exception = assertThrows(MockitoException.class, () ->
                    boardService.getBoardsBySortPreview("wrongEntity")
            );
            //then
            assertThat(exception.getMessage()).isEqualTo("PropertyReferenceException 에러 발생");
        }

        @Test
        @DisplayName("다른 사용자의 게시글에 접근")
        void createTempBoardAnotherUser() {
            //given
            List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                resourceRequestDtoList.add(ResourceRequestDto.builder()
                        .resourceName("재료 이름")
                        .amount("재료 수량")
                        .category("재료 카테고리")
                        .build()
                );
            }

            List<RecipeStepRequestDto> recipeStepRequestDtoList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                recipeStepRequestDtoList.add(RecipeStepRequestDto.builder()
                        .stepNum(i)
                        .stepContent("레시피 Step 내용")
                        .imageLink("레시피 이미지 Link")
                        .build()
                );
            }

            BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .mainImageLink("이미지 Link")
                    .resourceRequestDtoList(resourceRequestDtoList)
                    .recipeStepRequestDtoList(recipeStepRequestDtoList)
                    .build();

            Board board = Board.emptyBuilder()
                    .user(baseUser)
                    .build();

            User anotherUser = User.testRegister()
                    .email("email@test.com")
                    .password("password")
                    .userImg("userImgLink")
                    .provider(AuthProvider.kakao)
                    .providerId("providerId")
                    .nickname("nickname")
                    .role(UserRole.USER)
                    .userDescription("description")
                    .build();

            PrincipalDetails anotherUserDetails = new PrincipalDetails(anotherUser);

            //when
            when(userService.findUserByAuthEmail(
                    any(PrincipalDetails.class)
            )).thenReturn(anotherUser);

            when(boardRepository.findById(
                    anyLong()
            )).thenReturn(Optional.of(board));


            Exception exception = assertThrows(IdDifferentException.class, () ->
                    boardService.createTempBoard(anotherUserDetails, 1L, boardRequestDto)
            );
            //then
            assertThat(exception.getMessage()).isEqualTo(USER_ID_DIFFERENT);
        }

    }

}
