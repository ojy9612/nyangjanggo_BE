package com.hanghae99_team3.model.comment;

import com.hanghae99_team3.model.board.BoardRepository;
import com.hanghae99_team3.model.images.ImagesRepository;
import com.hanghae99_team3.model.recipestep.RecipeStepRepository;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.security.MockSpringSecurityFilter;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.security.Principal;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    MockMvc mockMvc;

    @MockBean
    AwsS3Service awsS3Service;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CommentService commentService;

    @MockBean
    BoardRepository boardRepository;

    @MockBean
    ImagesRepository imagesRepository;
    RecipeStepRepository recipeStepRepository;

    User baseUser;

    Principal mockPrincipal;
    PrincipalDetails baseUserDetails;
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();


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
        mockPrincipal = new UsernamePasswordAuthenticationToken(baseUserDetails, "", baseUserDetails.getAuthorities());
    }


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