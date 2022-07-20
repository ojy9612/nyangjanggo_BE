//package com.hanghae99_team3.model.board;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hanghae99_team3.docs.BoardDocumentation;
//import com.hanghae99_team3.login.jwt.JwtTokenProvider;
//import com.hanghae99_team3.login.jwt.PrincipalDetails;
//import com.hanghae99_team3.model.board.domain.Board;
//import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepMain;
//import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepRecipe;
//import com.hanghae99_team3.model.board.dto.request.BoardRequestDtoStepResource;
//import com.hanghae99_team3.model.board.repository.BoardRepository;
//import com.hanghae99_team3.model.board.service.BoardDocumentService;
//import com.hanghae99_team3.model.board.service.BoardService;
//import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
//import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
//import com.hanghae99_team3.model.user.domain.AuthProvider;
//import com.hanghae99_team3.model.user.domain.User;
//import com.hanghae99_team3.model.user.domain.UserRole;
//import com.hanghae99_team3.security.MockSpringSecurityFilter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@ExtendWith(RestDocumentationExtension.class)
//@MockBean(JpaMetamodelMappingContext.class)
//@WebMvcTest(BoardController.class)
//@DisplayName("Board 컨트롤러 테스트")
//class BoardControllerTest {
//
//    MockMvc mockMvc;
//    @MockBean JwtTokenProvider jwtTokenProvider;
//    @MockBean
//    BoardService boardService;
//    @MockBean
//    BoardDocumentService boardDocumentService;
//    @MockBean
//    BoardRepository boardRepository;
//    final String accessToken = "JwtAccessToken";
//    User baseUser;
//    Principal mockPrincipal;
//    PrincipalDetails baseUserDetails;
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext,
//                      RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))
//                .apply(documentationConfiguration(restDocumentation))
//                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
//                .alwaysDo(document("{method-name}",
//                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
//                .build();
//
//        baseUser = User.testRegister()
//                .email("email@test.com")
//                .password("password")
//                .userImg("userImgLink")
//                .provider(AuthProvider.kakao)
//                .providerId("providerId")
//                .nickname("nickname")
//                .role(UserRole.USER)
//                .userDescription("description")
//                .build();
//
//        baseUserDetails = new PrincipalDetails(baseUser);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(baseUserDetails, "", baseUserDetails.getAuthorities());
//    }
//
//
//    @Test
//    @DisplayName("Board 하나 불러오기")
//    void getOneBoard() throws Exception {
//        //given
//        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
//                .title("제목")
//                .subTitle("부제목")
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
//        when(boardService.getOneBoard(
//                anyLong()
//        ))
//                .thenReturn(board);
//
//        //then
//        mockMvc.perform(get("/api/board/{boardId}",1L)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.getOneBoard());
//    }
//
////    @Transactional
////    @Test
////    @DisplayName("Board 전체 불러오기")
////    void getAllBoards() throws Exception {
////        //given
////        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
////                .title("제목")
////                .subTitle("부제목")
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
//
//    @Test
//    @DisplayName("Board 생성 0단계 - 작성중인 파일 있을 때")
//    void createBoardStepStart() throws Exception {
//        //given
//        BoardRequestDtoStepMain boardRequestDtoStepMain = BoardRequestDtoStepMain.builder()
//                .title("제목")
//                .subTitle("부제목")
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
//                .subTitle("레시피 설명")
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
//                .subTitle("레시피 설명")
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
//
//
//    @Test
//    @DisplayName("Board 삭제")
//    void deleteBoard() throws Exception {
//        //given
//        //when
//        doNothing().when(boardService);
//
//        //then
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/board/{boardID}",1L)
//                        .header("Access-Token", accessToken)
//                        .principal(mockPrincipal)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(BoardDocumentation.deleteBoard());
//
//    }
//
//}
