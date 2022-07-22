package com.hanghae99_team3.model.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.dto.request.BoardRequestDto;
import com.hanghae99_team3.model.board.service.BoardDocumentService;
import com.hanghae99_team3.model.board.service.BoardService;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.security.MockSpringSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BoardController.class)
@DisplayName("Board 컨트롤러 테스트")
class BoardControllerTest {

    MockMvc mockMvc;
    @MockBean
    JwtTokenProvider jwtTokenProvider;
    @MockBean
    BoardService boardService;
    @MockBean
    BoardDocumentService boardDocumentService;
    final String accessToken = "JwtAccessToken";
    User baseUser;
    Principal mockPrincipal;
    PrincipalDetails baseUserDetails;
    ObjectMapper objectMapper = new ObjectMapper();


    static FieldDescriptor[] boardDetailResponseDto = new FieldDescriptor[] {
            fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
            fieldWithPath("status").type(JsonFieldType.STRING).description("게시글 상태(modifying,complete)").optional(),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임").optional(),
            fieldWithPath("userImg").type(JsonFieldType.STRING).description("유저 이미지").optional(),
            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목").optional(),
            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
            fieldWithPath("mainImg").type(JsonFieldType.STRING).description("게시글 대표 이미지").optional(),
            fieldWithPath("resourceResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("resourceResponseDtoList[].resourceName").type(JsonFieldType.STRING).description("재료 이름").optional(),
            fieldWithPath("resourceResponseDtoList[].amount").type(JsonFieldType.STRING).description("재료 수량").optional(),
            fieldWithPath("resourceResponseDtoList[].category").type(JsonFieldType.STRING).description("재료 카테고리").optional(),
            fieldWithPath("recipeStepResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("recipeStepResponseDtoList[].stepNum").type(JsonFieldType.NUMBER).description("레시피 Step").optional(),
            fieldWithPath("recipeStepResponseDtoList[].content").type(JsonFieldType.STRING).description("레시피 내용").optional(),
            fieldWithPath("recipeStepResponseDtoList[].imageLink").type(JsonFieldType.STRING).description("레시피 사진").optional(),
            fieldWithPath("commentList").type(JsonFieldType.ARRAY).description("댓글 리스트").optional(),
            fieldWithPath("commentList[].nickname").type(JsonFieldType.NUMBER).description("댓글 유저 닉네임").optional(),
            fieldWithPath("commentList[].userImg").type(JsonFieldType.STRING).description("댓글 유저 이미지").optional(),
            fieldWithPath("commentList[].comment").type(JsonFieldType.STRING).description("댓글 내용").optional(),
            fieldWithPath("commentList[].createdAt").type(JsonFieldType.STRING).description("댓글 생성일자").optional(),
            fieldWithPath("commentList[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정일자").optional(),
            fieldWithPath("goodList").type(JsonFieldType.ARRAY).description("좋아요 리스트").optional(),
            fieldWithPath("goodList[].nickname").type(JsonFieldType.NUMBER).description("좋아요 유저 닉네임").optional(),
            fieldWithPath("goodList[].userImg").type(JsonFieldType.STRING).description("좋아요 유저 이미지").optional(),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글 생성 날짜").optional(),
            fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시글 수정 날짜").optional()
    };

    static FieldDescriptor[] listBoardResponseDto = new FieldDescriptor[]{
            fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
            fieldWithPath("[].status").type(JsonFieldType.STRING).description("게시글 상태(modifying, complete)").optional(),
            fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("유저 닉네임").optional(),
            fieldWithPath("[].userImg").type(JsonFieldType.STRING).description("유저 이미지").optional(),
            fieldWithPath("[].title").type(JsonFieldType.STRING).description("게시글 제목").optional(),
            fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
            fieldWithPath("[].mainImg").type(JsonFieldType.STRING).description("게시글 대표 이미지").optional(),
            fieldWithPath("[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수").optional(),
            fieldWithPath("[].goodCount").type(JsonFieldType.NUMBER).description("좋아요 수").optional(),
            fieldWithPath("[].resourceResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("[].resourceResponseDtoList[].resourceName").type(JsonFieldType.STRING).description("재료 이름").optional(),
            fieldWithPath("[].resourceResponseDtoList[].amount").type(JsonFieldType.STRING).description("재료 수량").optional(),
            fieldWithPath("[].resourceResponseDtoList[].category").type(JsonFieldType.STRING).description("재료 카테고리").optional(),
            fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("게시글 생성 날짜").optional(),
            fieldWithPath("[].modifiedAt").type(JsonFieldType.STRING).description("게시글 수정 날짜").optional(),
    };

    static FieldDescriptor[] pageBoardResponseDto = new FieldDescriptor[] {
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("내용 List").optional(),
            fieldWithPath("content[].boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
            fieldWithPath("content[].status").type(JsonFieldType.STRING).description("게시글 상태(modifying, complete)").optional(),
            fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("유저 닉네임").optional(),
            fieldWithPath("content[].userImg").type(JsonFieldType.STRING).description("유저 이미지").optional(),
            fieldWithPath("content[].title").type(JsonFieldType.STRING).description("게시글 제목").optional(),
            fieldWithPath("content[].content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
            fieldWithPath("content[].mainImg").type(JsonFieldType.STRING).description("게시글 대표 이미지").optional(),
            fieldWithPath("content[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수").optional(),
            fieldWithPath("content[].goodCount").type(JsonFieldType.NUMBER).description("좋아요 수").optional(),
            fieldWithPath("content[].resourceResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("content[].resourceResponseDtoList[].resourceName").type(JsonFieldType.STRING).description("재료 이름").optional(),
            fieldWithPath("content[].resourceResponseDtoList[].amount").type(JsonFieldType.STRING).description("재료 수량").optional(),
            fieldWithPath("content[].resourceResponseDtoList[].category").type(JsonFieldType.STRING).description("재료 카테고리").optional(),
            fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("게시글 생성 날짜").optional(),
            fieldWithPath("content[].modifiedAt").type(JsonFieldType.STRING).description("게시글 수정 날짜").optional(),
            fieldWithPath("pageable").type(JsonFieldType.STRING).description("").optional(),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지면 True").optional(),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수").optional(),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수").optional(),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("한 페이지에 보여지는 데이터 수").optional(),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호(0부터 시작)").optional(),
            fieldWithPath("sort").type(JsonFieldType.OBJECT).description("").optional(),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("").optional(),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("").optional(),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("").optional(),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 수").optional(),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지면 True").optional(),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("데이터가 0개인 경우 True").optional()
    };

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

    @Test
    @DisplayName("Board 하나 불러오기")
    void getOneBoard() throws Exception {
        //given
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

        Board board = Board.builder()
                .boardRequestDto(boardRequestDto)
                .user(baseUser)
                .build();

        //when
        when(boardService.findBoardById(
                anyLong()
        ))
                .thenReturn(board);

        ResultActions resultActions = mockMvc.perform(
                get("/api/board/{boardId}", 1L));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_getOneBoard",
                        responseFields(
                            boardDetailResponseDto
                        )
                ));
    }

    @Test
    @DisplayName("조건별 게시글 전체 조회")
    void getAllBoardsBySort() throws Exception {
        //given
        List<Board> boardList = new ArrayList<>();
        for(int i = 0; i < 15; i ++){
            boardList.add(Board.emptyBuilder()
                    .user(baseUser)
                    .build()
            );
        }

        Page<Board> boardPage = new PageImpl<>(boardList);

        //when
        when(boardService.getAllBoardsBySort(
                any(Pageable.class),
                anyString()
        ))
                .thenReturn(boardPage);

        ResultActions resultActions = mockMvc.perform(
                get("/api/boards?entityName=goodCount&page=0&size=5"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_getAllBoardsBySort",
                        requestParameters(
                                parameterWithName("entityName").description("정렬하고자 하는 Entity 의 이름(goodCount,createdAt)").optional(),
                                parameterWithName("page").description("페이지 번호(0부터 시작)").optional(),
                                parameterWithName("size").description("한 페이지에 불러올 게시글 수").optional()
                        ),
                        responseFields(
                                pageBoardResponseDto
                        )
                ));
    }

    @Test
    @DisplayName("조건별 게시글 10개만 불러오기")
    void getBoardsBySortPreview() throws Exception {
        //given

        List<Board> boardList = new ArrayList<>();
        for (int i = 0 ; i < 15; i++){
            boardList.add(Board.emptyBuilder()
                    .user(baseUser)
                    .build()
            );
        }

        //when
        when(boardService.getBoardsBySortPreview(
                anyString()
        ))
                .thenReturn(boardList);

        ResultActions resultActions = mockMvc.perform(
                get("/api/boards/preview?entityName=goodCount"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_getBoardsBySortPreview",
                        requestParameters(
                                parameterWithName("entityName").description("정렬하고자 하는 Entity 의 이름(goodCount,createdAt)").optional()
                        ),
                        responseFields(
                                listBoardResponseDto
                        )
                ));
    }

    @Test
    @DisplayName("재료기반 게시글 검색시 추천 검색어")
    void recommendResource() throws Exception {
        //given
        List<String> stringList = new ArrayList<>();
        stringList.add("재료 A");
        stringList.add("재료 B");
        stringList.add("재료 C");

        //when
        when(boardDocumentService.recommendResource(
                anyString()
        ))
                .thenReturn(stringList);

        ResultActions resultActions = mockMvc.perform(
                get("/api/board/resource/recommend?resourceName=재료이름"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_recommendResource",
                        requestParameters(
                                parameterWithName("resourceName").description("검색어(재료이름)").optional()
                        ),
                        responseFields(
                            fieldWithPath("resourceRecommendList").description("재료 리스트")
                        )
                ));
    }

    @Test
    @DisplayName("재료기반 게시글 검색")
    void getBoardDocumentsByResource() throws Exception {
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
        when(boardDocumentService.getBoardDocumentsByResource(
                anyString(),
                any(Pageable.class)
        ))
                .thenReturn(boardPage);

        ResultActions resultActions = mockMvc.perform(
                get("/api/boards/resource?resourceName=재료이름&page=0&size=5"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_getBoardDocumentsByResource",
                        requestParameters(
                                parameterWithName("resourceName").description("검색어(재료이름)").optional(),
                                parameterWithName("page").description("페이지 번호(0부터 시작)").optional(),
                                parameterWithName("size").description("한 페이지에 불러올 게시글 수").optional()
                        ),
                        responseFields(
                            pageBoardResponseDto
                        )
                ));
    }

    @Test
    @DisplayName("제목기반 게시글 검색시 추천 검색어")
    void recommendBoardDocumentByTitle() throws Exception {
        //given
        List<String> stringList = new ArrayList<>();
        stringList.add("게시글 A");
        stringList.add("게시글 B");
        stringList.add("게시글 C");

        //when
        when(boardDocumentService.recommendBoardDocumentByTitle(
                anyString()
        ))
                .thenReturn(stringList);

        ResultActions resultActions = mockMvc.perform(
                get("/api/board/title/recommend?titleWords=게시글제목"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_recommendBoardDocumentByTitle",
                        requestParameters(
                                parameterWithName("titleWords").description("검색어(게시글이름)").optional()
                        ),
                        responseFields(
                            fieldWithPath("titleRecommendList").description("게시글 제목 리스트")
                        )
                ));
    }

    @Test
    @DisplayName("제목기반 게시글 검색")
    void getBoardDocumentsByTitle() throws Exception {
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
        when(boardDocumentService.getBoardDocumentsByTitle(
                anyString(),
                any(Pageable.class)
        ))
                .thenReturn(boardPage);

        ResultActions resultActions = mockMvc.perform(
                get("/api/boards/title?titleWords=게시글제목&page=0&size=5"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_getBoardDocumentsByTitle",
                        requestParameters(
                                parameterWithName("titleWords").description("검색어(게시글제목)").optional(),
                                parameterWithName("page").description("페이지 번호(0부터 시작)").optional(),
                                parameterWithName("size").description("한 페이지에 불러올 게시글 수").optional()
                        ),
                        responseFields(
                                pageBoardResponseDto
                        )
                ));
    }


    @Test
    @DisplayName("Board 이미지 등록")
    void createImage() throws Exception {
        //given
        MockMultipartFile image = new MockMultipartFile(
                "multipartFile",
                "test1.jpg",
                "image/jpg",
                "<<image data>>".getBytes(StandardCharsets.UTF_8)
        );

        //when
        when(boardService.createImage(
                any(MultipartFile.class),
                anyLong()
        ))
                .thenReturn("이미지 Link");

        MockMultipartHttpServletRequestBuilder builder =
                multipart("/api/board/image?boardId=1");

        ResultActions resultActions = mockMvc.perform(builder
                .file(image)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_createImage",
                        requestParameters(
                                parameterWithName("boardId").description("게시글ID").optional()
                        ),
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        ),
                        requestParts(
                                partWithName("multipartFile").description("이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("imageLink").type(JsonFieldType.STRING).description("이미지 Link")
                        )
                ));
    }

    @Test
    @DisplayName("작성중인 게시글 확인")
    void checkModifyingBoard() throws Exception {
        //given
        Board board = Board.emptyBuilder()
                .user(baseUser)
                .build();

        //when
        when(boardService.checkModifyingBoard(
            any(PrincipalDetails.class)
        ))
                .thenReturn(board);

        MockMultipartHttpServletRequestBuilder builder =
                multipart("/api/board/check");
        builder.with(request -> {
            request.setMethod("GET");
            return request;
        });

        ResultActions resultActions = mockMvc.perform(builder
                .header("Access-Token", accessToken)
                .principal(mockPrincipal)
        );
        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_checkModifyingBoard",
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        ),
                        responseFields(
                            boardDetailResponseDto
                        )
                ));
    }

    @Test
    @DisplayName("임시(작성중인) 게시글 저장")
    void createTempBoard() throws Exception {
        //given
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

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "boardRequestDto",
                "boardRequestDto",
                "application/json",
                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(boardService);

        MockMultipartHttpServletRequestBuilder builder =
                multipart("/api/board/temp?boardId=1");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        ResultActions resultActions = mockMvc.perform(builder
                .file(mockMultipartFile)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_createTempBoard",
                        requestParameters(
                                parameterWithName("boardId").description("게시글ID").optional()
                        ),
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        ),
                        requestParts(
                                partWithName("boardRequestDto").description(
                                        objectMapper.writeValueAsString(boardRequestDto))
                        )
                ));
    }

    @Test
    @DisplayName("게시글 생성")
    void createBoard() throws Exception {
        //given
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

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "boardRequestDto",
                "boardRequestDto",
                "application/json",
                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(boardService);

        MockMultipartHttpServletRequestBuilder builder =
                multipart("/api/board?boardId=1");
        builder.with(request -> {
            request.setMethod("POST");
            return request;
        });

        ResultActions resultActions = mockMvc.perform(builder
                .file(mockMultipartFile)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_createBoard",
                        requestParameters(
                                parameterWithName("boardId").description("게시글ID").optional()
                        ),
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        ),
                        requestParts(
                                partWithName("boardRequestDto").description(
                                        objectMapper.writeValueAsString(boardRequestDto))
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정")
    void updateBoard() throws Exception {
        //given
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

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "boardRequestDto",
                "boardRequestDto",
                "application/json",
                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(boardService);

        MockMultipartHttpServletRequestBuilder builder =
                multipart("/api/board?boardId=1");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        ResultActions resultActions = mockMvc.perform(builder
                .file(mockMultipartFile)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_updateBoard",
                        requestParameters(
                                parameterWithName("boardId").description("게시글ID").optional()
                        ),
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        ),
                        requestParts(
                                partWithName("boardRequestDto").description(
                                        objectMapper.writeValueAsString(boardRequestDto))
                        )
                ));
    }

    @Test
    @DisplayName("Board 삭제")
    void deleteBoard() throws Exception {
        //given
        //when
        doNothing().when(boardService);

        ResultActions resultActions = mockMvc.perform(
                delete("/api/board?boardId=1")
                        .header("Access-Token", accessToken)
                        .principal(mockPrincipal)
        );
        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("R_deleteBoard",
                        requestParameters(
                                parameterWithName("boardId").description("게시글ID").optional()
                        ),
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        )
                ));

    }


////    @Transactional
////    @Test
////    @DisplayName("Board 전체 불러오기")
////    void getAllBoards() throws Exception {
////        //given
////        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
////                .title("제목")
////                .content("내용")
////                .build();
////
////        Board board = Board.builder()
////                .boardRequestDtoStepMain(boardRequestDtoStepMain)
////                .mainImage("이미지Url")
////                .status("complete")
////                .user(baseUser)
////                .build();
////
////        boardRepository.save(board);
////
////        //when
////
////        //then
////        mockMvc.perform(get("/api/boards")
////                )
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andDo(BoardDocumentation.getAllBoards());
////    }


//    @Test
//    @DisplayName("Board 생성 0단계 - 작성중인 파일 있을 때")
//    void createBoardStepStart() throws Exception {
//        //given
//        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
//                .title("제목")
//                .("부제목")
//                .content("내용")
//                .build();
//
//        Board board = Board.builder()
//                .boardRequestDtoStepMain(boardRequestDtoStepMain)
//                .mainImage("이미지Url")
//                .status("step 1")
//                .user(baseUser)
//                .build();
//
//        //when
//        when(boardService.createBoardStepStart(
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(Optional.of(board));
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/0");
//        builder.with(request -> {
//            request.setMethod("GET");
//            return request;
//        });
//
//        //then
//        mockMvc.perform(builder
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.createBoardStepStart());
//    }
//
//
//    @Test
//    @DisplayName("Board 생성 1단계 - 게시글 정보 등록")
//    void createBoardStepMain() throws Exception {
//        //given
//        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
//                .title("제목")
//                .content("내용")
//                .build();
//
////            File file = new File("src/test/resources/image/"+
////                    "test1.jpg");
////            FileItem fileItem = new DiskFileItem("test1.jpg",
////                    Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
////
////            try {
////                IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
////            } catch (IOException ex) {
////                System.err.println("에러다 에러 ! ex.getMessage() = " + ex.getMessage());
////            }
////
////            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "boardRequestDtoStepMain",
//                "boardRequestDtoStepMain",
//                "application/json",
//                objectMapper.writeValueAsString(boardRequestDtoStepMain).getBytes(StandardCharsets.UTF_8)
//        );
//
//        MockMultipartFile image = new MockMultipartFile(
//                "multipartFile",
//                "test1.jpg",
//                "image/jpg",
//                "<<image data>>".getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        when(boardService.createBoardStepMain(
//                any(BoardRequestDtoStepMain.class),
//                any(MultipartFile.class),
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(1L);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/1");
//
//        //then
//        mockMvc.perform(builder
//                        .file(image)
//                        .file(mockMultipartFile)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.createBoardStepMain(boardRequestDtoStepMain));
//
//    }
//
//
//    @Test
//    @DisplayName("Board 생성 2단계 - 재료 등록")
//    void createBoardStepResource() throws Exception {
//        //given
//        List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();
//        for (int i = 0; i < 2; i++){
//            resourceRequestDtoList.add(ResourceRequestDto.builder()
//                    .resourceName("재료"+i)
//                    .amount("수량"+i)
//                    .category("카테고리"+i)
//                    .build()
//            );
//        }
//
//        BoardRequestDtoStepResource boardRequestDtoStepResource = BoardRequestDtoStepResource.builder()
//                .boardId(1L)
//                .resourceRequestDtoList(resourceRequestDtoList)
//                .build();
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "boardRequestDtoStepResource",
//                "boardRequestDtoStepResource",
//                "application/json",
//                objectMapper.writeValueAsString(boardRequestDtoStepResource).getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        when(boardService.createBoardStepResource(
//                any(BoardRequestDtoStepResource.class),
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(1L);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/2");
//
//        //then
//        mockMvc.perform(builder
//                        .file(mockMultipartFile)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.createBoardStepResource(boardRequestDtoStepResource));
//
//    }
//
//
//    @Test
//    @DisplayName("Board 생성 3단계 - 게시글 레시피 등록")
//    void createBoardStepRecipe() throws Exception {
//        //given
//        RecipeStepRequestDto recipeStepRequestDto = RecipeStepRequestDto.builder()
//                .stepContent("Step 내용")
//                .stepNum(1)
//                .build();
//
//        BoardRequestDtoStepRecipe boardRequestDtoStepRecipe = BoardRequestDtoStepRecipe.builder()
//                .boardId(1L)
//                .recipeStepRequestDto(recipeStepRequestDto)
//                .build();
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "boardRequestDtoStepRecipe",
//                "boardRequestDtoStepRecipe",
//                "application/json",
//                objectMapper.writeValueAsString(boardRequestDtoStepRecipe).getBytes(StandardCharsets.UTF_8)
//        );
//
//        MockMultipartFile image = new MockMultipartFile(
//                "multipartFile",
//                "test1.jpg",
//                "image/jpg",
//                "<<image data>>".getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        when(boardService.createBoardStepRecipe(
//                any(BoardRequestDtoStepRecipe.class),
//                any(MultipartFile.class),
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(1L);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/3");
//
//        //then
//        mockMvc.perform(builder
//                        .file(image)
//                        .file(mockMultipartFile)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.createBoardStepRecipe(boardRequestDtoStepRecipe));
//
//    }
//
//
//    @Test
//    @DisplayName("Board 생성 4단계 - 게시글 등록")
//    void createBoardStepEnd() throws Exception {
//        //given
//        Long boardId = 1L;
//
//        MockMultipartFile mockBoardId = new MockMultipartFile(
//                "boardId",
//                "boardId",
//                "application/json",
//                objectMapper.writeValueAsString(boardId).getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        doNothing().when(boardService);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/-1");
//
//        //then
//        mockMvc.perform(builder
//                        .file(mockBoardId)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.createBoardStepEnd());
//
//    }
//
//
//    @Test
//    @DisplayName("Board 수정 1단계 - 게시글 정보 수정")
//    void updateBoardStepMain() throws Exception {
//        //given
//        Long boardId = 1L;
//
//        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
//                .title("제목")
//                .content("내용")
//                .build();
//
//        MockMultipartFile mockBoardId = new MockMultipartFile(
//                "boardId",
//                "boardId",
//                "application/json",
//                objectMapper.writeValueAsString(boardId).getBytes(StandardCharsets.UTF_8)
//        );
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "boardRequestDtoStepMain",
//                "boardRequestDtoStepMain",
//                "application/json",
//                objectMapper.writeValueAsString(boardRequestDtoStepMain).getBytes(StandardCharsets.UTF_8)
//        );
//
//        MockMultipartFile image = new MockMultipartFile(
//                "multipartFile",
//                "test1.jpg",
//                "image/jpg",
//                "<<image data>>".getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        when(boardService.updateBoardStepMain(
//                any(Long.class),
//                any(BoardRequestDtoStepMain.class),
//                any(MultipartFile.class),
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(1L);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/1");
//        builder.with(request -> {
//            request.setMethod("PUT");
//            return request;
//        });
//
//        //then
//        mockMvc.perform(builder
//                        .file(mockBoardId)
//                        .file(image)
//                        .file(mockMultipartFile)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.updateBoardStepMain(boardRequestDtoStepMain));
//
//    }
//
//
//
//    @Test
//    @DisplayName("Board 수정 2단계 - 재료 수정")
//    void updateBoardStepResource() throws Exception {
//        //given
//        List<ResourceRequestDto> resourceRequestDtoList = new ArrayList<>();
//        for (int i = 0; i < 2; i++){
//            resourceRequestDtoList.add(ResourceRequestDto.builder()
//                    .resourceName("재료"+i)
//                    .amount("수량"+i)
//                    .category("카테고리"+i)
//                    .build()
//            );
//        }
//
//        BoardRequestDtoStepResource boardRequestDtoStepResource = BoardRequestDtoStepResource.builder()
//                .boardId(1L)
//                .resourceRequestDtoList(resourceRequestDtoList)
//                .build();
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "boardRequestDtoStepResource",
//                "boardRequestDtoStepResource",
//                "application/json",
//                objectMapper.writeValueAsString(boardRequestDtoStepResource).getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        when(boardService.updateBoardStepResource(
//                any(BoardRequestDtoStepResource.class),
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(1L);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/2");
//        builder.with(request -> {
//            request.setMethod("PUT");
//            return request;
//        });
//
//        //then
//        mockMvc.perform(builder
//                        .file(mockMultipartFile)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.updateBoardStepResource(boardRequestDtoStepResource));
//
//    }
//
//
//    @Test
//    @DisplayName("Board 수정 3단계 - 게시글 레시피 수정")
//    void updateBoardStepRecipe() throws Exception {
//        //given
//        RecipeStepRequestDto recipeStepRequestDto = RecipeStepRequestDto.builder()
//                .stepContent("Step 내용")
//                .stepNum(1)
//                .build();
//
//        BoardRequestDtoStepRecipe boardRequestDtoStepRecipe = BoardRequestDtoStepRecipe.builder()
//                .boardId(1L)
//                .recipeStepRequestDto(recipeStepRequestDto)
//                .build();
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "boardRequestDtoStepRecipe",
//                "boardRequestDtoStepRecipe",
//                "application/json",
//                objectMapper.writeValueAsString(boardRequestDtoStepRecipe).getBytes(StandardCharsets.UTF_8)
//        );
//
//        MockMultipartFile image = new MockMultipartFile(
//                "multipartFile",
//                "test1.jpg",
//                "image/jpg",
//                "<<image data>>".getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        when(boardService.updateBoardStepRecipe(
//                any(BoardRequestDtoStepRecipe.class),
//                any(MultipartFile.class),
//                any(PrincipalDetails.class)
//        ))
//                .thenReturn(1L);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/3");
//        builder.with(request -> {
//            request.setMethod("PUT");
//            return request;
//        });
//
//        //then
//        mockMvc.perform(builder
//                        .file(image)
//                        .file(mockMultipartFile)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.updateBoardStepRecipe(boardRequestDtoStepRecipe));
//
//    }
//
//
//    @Test
//    @DisplayName("Board 삭제 3단계 - 게시글 레시피 삭제")
//    void deleteBoardStepRecipe() throws Exception {
//        //given
//        Long boardId = 1L;
//
//        Integer stepNum = 0;
//
//        MockMultipartFile mockBoardId = new MockMultipartFile(
//                "boardId",
//                "boardId",
//                "application/json",
//                objectMapper.writeValueAsString(boardId).getBytes(StandardCharsets.UTF_8)
//        );
//
//        MockMultipartFile mockStepNum = new MockMultipartFile(
//                "stepNum",
//                "stepNum",
//                "application/json",
//                objectMapper.writeValueAsString(stepNum).getBytes(StandardCharsets.UTF_8)
//        );
//
//        //when
//        doNothing().when(boardService);
//
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/board/step/3");
//        builder.with(request -> {
//            request.setMethod("DELETE");
//            return request;
//        });
//
//        //then
//        mockMvc.perform(builder
//                        .file(mockBoardId)
//                        .file(mockStepNum)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.deleteRecipeStep());
//
//    }

}
