package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.board.BoardRepository;
import com.hanghae99_team3.model.images.Images;
import com.hanghae99_team3.model.images.ImagesRepository;
import com.hanghae99_team3.model.recipestep.RecipeStep;
import com.hanghae99_team3.model.recipestep.RecipeStepRepository;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class CommentControllerTest {
    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ImagesRepository imagesRepository;
    RecipeStepRepository recipeStepRepository;

    User baseUser;
    PrincipalDetails basePrincipalDetails;
    Board baseBoard;

    @Nested
    @DisplayName("댓글 기능 Test")
    class CommentTest {

//        @BeforeEach
//        void setUp(){
//            //given
//            baseUser = User.userDetailRegister()
//                    .email("email@test.com")
//                    .password("password!")
//                    .username("nickname")
//                    .role(UserRole.USER)
//                    .build();
//
//            basePrincipalDetails = new PrincipalDetails(baseUser);
//
//            List<MultipartFile> baseMultipaerFileList = new ArrayList<>();
//
//
//            BoardRequestDto boardRequestDto = BoardRequestDto.builder()
//                    .title("제목")
//                    .subTitle("부제목")
//                    .content("내용")
//                    .resourceInfos()
//                    .imgFile()
//                    .build();
//
//            baseBoard = Board.builder()
//                    .boardRequestDto(boardRequestDto)
//                    .user(baseUser)
//                    .build();
//
//            //when
//            userRepository.save(baseUser);
//            boardRepository.save(baseBoard);
//        }

        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest {

            @Test
            @Transactional
            @DisplayName("댓글 생성")
            void CreateComment(){
//                List<String> allObject = awsS3Service.getAllObject().get(0);
//
//                allObject.forEach(imageLink -> {
//                    Optional<Images> optionalImages1 = imagesRepository.findByImageLink(imageLink);
//                    Optional<Board> optionalImages2 = boardRepository.findByMainImage(imageLink);
//                    Optional<RecipeStep> optionalImages3 = recipeStepRepository.findByImageLink(imageLink);
//
//                    if (optionalImages1.isEmpty() && optionalImages2.isEmpty() && optionalImages3.isEmpty()){
//                        awsS3Service.deleteFile(imageLink);
//                    }
//                });



                //given
//                Comment comment = Comment.builder()
//                        .board()
//                        .build()
                //when

                //then

            }

        }
    }

}