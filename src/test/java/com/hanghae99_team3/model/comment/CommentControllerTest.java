package com.hanghae99_team3.model.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.docs.CommentDocumentation;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.security.MockSpringSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CommentController.class)
@DisplayName("Comment 컨트롤러 테스트")
class CommentControllerTest {

    MockMvc mockMvc;
    @MockBean JwtTokenProvider jwtTokenProvider;
    @MockBean UserRepository userRepository;
    @MockBean CommentService commentService;
    @MockBean CommentRepository commentRepository;
    final String accessToken = "JwtAccessToken";
    User baseUser;
    Principal mockPrincipal;
    PrincipalDetails baseUserDetails;
    Board baseBoard;
    ObjectMapper objectMapper = new ObjectMapper();

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

        List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();

        for(int i = 0; i < 2; i ++){
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

        baseBoard = Board.builder()
                .boardRequestDto(boardRequestDto)
                .user(baseUser)
                .build();

    }

    @Test
    @DisplayName("댓글 등록")
    void createComment() throws Exception {
        //given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("댓글 내용")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "commentRequestDto",
                "commentRequestDto",
                "application/json",
                objectMapper.writeValueAsString(commentRequestDto).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(commentService);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/board/{boardId}/comment",1L);

        //then
        mockMvc.perform(builder
                        .file(mockMultipartFile)
                        .header("Access-Token", accessToken)
                        .principal(mockPrincipal)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.createComment(commentRequestDto));

    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() throws Exception {
        //given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("댓글 내용")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "commentRequestDto",
                "commentRequestDto",
                "application/json",
                objectMapper.writeValueAsString(commentRequestDto).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(commentService);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/board/{boardId}/comment/{commentId}",1L,1L);
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        //then
        mockMvc.perform(builder
                        .file(mockMultipartFile)
                        .header("Access-Token", accessToken)
                        .principal(mockPrincipal)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.updateComment(commentRequestDto));

    }

    @Test
    @DisplayName("댓글 삭제")
    void removeComment() throws Exception {
        //given
        //when
        doNothing().when(commentService);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/board/{boardId}/comment/{commentId}",1L,1L);
        builder.with(request -> {
            request.setMethod("DELETE");
            return request;
        });

        //then
        mockMvc.perform(builder
                        .header("Access-Token", accessToken)
                        .principal(mockPrincipal)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.removeComment());

    }

}