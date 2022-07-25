package com.hanghae99_team3.model.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.model.fridge.Fridge;
import com.hanghae99_team3.model.fridge.FridgeService;
import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
import com.hanghae99_team3.model.fridge.dto.FridgeResponseDto;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.security.MockSpringSecurityFilter;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class) // JUnit5에서 필요
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @MockBean private UserRepository userRepository;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private UserService userService;
    @MockBean private FridgeService fridgeService;
    private final String accessToken = "JwtAccessToken";
    private User testUser;
    private Principal mockPrincipal;
    ObjectMapper objectMapper = new ObjectMapper();

    // MockMvc, Spring Rest Docs Setup
    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
//                .apply(SecurityMockMvcConfigurers.springSecurity(new JwtAuthFilter(jwtTokenProvider)))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();

        testUser = User.testRegister()
                .email("email@test.com")
                .password("password")
                .userImg("userImgLink")
                .provider(AuthProvider.kakao)
                .providerId("providerId")
                .nickname("nickname")
                .role(UserRole.USER)
                .userDescription("description")
                .build();

        PrincipalDetails testUserDetails = new PrincipalDetails(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("회원정보 조회")
    void getUser() throws Exception {
        //given
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));

        //when
        ResultActions resultActions = this.mockMvc.perform(get("/api/user")
                        .header("Access-Token", accessToken)
                        .principal(mockPrincipal));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-user",
                        requestHeaders(headerWithName("Access-Token").description("Jwt Access-Token")),
                        responseFields(
                                fieldWithPath("nickname").description("회원 닉네임"),
                                fieldWithPath("userImg").description("회원 프로필 사진 링크"),
                                fieldWithPath("userDescription").description("회원 소개글")
                        )

                ));

    }
//
//    @Test
//    @DisplayName("회원정보 수정")
//    void updateUser() throws Exception {
//        //given
//        MockMultipartFile image = new MockMultipartFile(
//                "userImg",
//                "userImg.png",
//                "image/png",
//                "<<png data>>".getBytes(StandardCharsets.UTF_8));
//
//        //when
//        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/user");
//        builder.with(request -> {
//            request.setMethod("PUT");
//            return request;
//        });
//
//        ResultActions resultActions = this.mockMvc.perform(builder
//                .file(image)
//                        .param("nickname", "nickname")
//                        .param("userDescription", "userDescription")
//                .header("Access-Token", accessToken)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .principal(mockPrincipal));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andDo(print())
//                .andDo(document("put-user",
//                        requestHeaders(
//                                headerWithName("Access-Token").description("Jwt Access-Token")
//                        ),
//                        requestParameters(
//                                parameterWithName("nickname").description("변경할 닉네임"),
//                                parameterWithName("userDescription").description("변경할 유저 소개글")
//                        ),
//                        requestParts(
//                                partWithName("userImg").description("변경할 프로필 이미지 ")
//                        )
//                ));
//
//    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser() throws Exception {
        //given
        //when
        ResultActions resultActions = this.mockMvc.perform(delete("/api/user")
                .header("Access-Token", accessToken)
                .principal(mockPrincipal));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-user",
                        requestHeaders(headerWithName("Access-Token").description("Jwt Access-Token"))
                ));
    }

    @Test
    @DisplayName("냉장고 조회")
    void getFridge() throws Exception {
        //given
        List<FridgeRequestDto> fridgeRequestDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fridgeRequestDtoList.add(FridgeRequestDto.builder()
                    .resourceName("재료" + i)
                    .amount("수량" + i)
                    .category("카테고리" + i)
                    .endTime("유통기한" + i)
                    .build()
            );
        }

        List<Fridge> fridgeList = fridgeRequestDtoList.stream().map(fridgeRequestDto -> Fridge.builder()
                .fridgeRequestDto(fridgeRequestDto)
                .user(testUser)
                .build()
        ).collect(Collectors.toList());

        //when
        when(userService.getFridge(
                any(PrincipalDetails.class))
        )
                .thenReturn(fridgeList);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/user/fridge");
        builder.with(request -> {
            request.setMethod("GET");
            return request;
        });

        ResultActions resultActions = this.mockMvc.perform(builder
                .header("Access-Token", accessToken)
                .principal(mockPrincipal));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-getFridge",
                        requestHeaders(headerWithName("Access-Token").description("Jwt Access-Token")),
                        responseFields(
                                fieldWithPath("[].resourceName").type(JsonFieldType.STRING).description("재료 이름"),
                                fieldWithPath("[].amount").type(JsonFieldType.STRING).description("재료 수량"),
                                fieldWithPath("[].category").type(JsonFieldType.STRING).description("재료 카테고리"),
                                fieldWithPath("[].endTime").type(JsonFieldType.STRING).description("재료 유효기간")
                                )
                ));
    }


    @Test
    @DisplayName("냉장고 등록")
    void createFridge() throws Exception {
        //given
        List<FridgeRequestDto> fridgeRequestDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fridgeRequestDtoList.add(FridgeRequestDto.builder()
                    .resourceName("재료" + i)
                    .amount("수량" + i)
                    .category("카테고리" + i)
                    .endTime("유통기한" + i)
                    .build()
            );
        }

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "fridgeRequestDtoList",
                "fridgeRequestDtoList",
                "application/json",
                objectMapper.writeValueAsString(fridgeRequestDtoList).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(userService);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/user/fridge");
        builder.with(request -> {
            request.setMethod("POST");
            return request;
        });

        ResultActions resultActions = this.mockMvc.perform(builder
                .file(mockMultipartFile)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-createFridge",
                        requestHeaders(headerWithName("Access-Token").description("Jwt Access-Token")),
                        requestParts(partWithName("fridgeRequestDtoList").description(objectMapper.writeValueAsString(fridgeRequestDtoList)))
                ));
    }

    @Test
    @DisplayName("냉장고 수정")
    void updateFridge() throws Exception {
        //given
        List<FridgeRequestDto> fridgeRequestDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fridgeRequestDtoList.add(FridgeRequestDto.builder()
                    .resourceName("재료" + i)
                    .amount("수량" + i)
                    .category("카테고리" + i)
                    .endTime("유통기한" + i)
                    .build()
            );
        }

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "fridgeRequestDtoList",
                "fridgeRequestDtoList",
                "application/json",
                objectMapper.writeValueAsString(fridgeRequestDtoList).getBytes(StandardCharsets.UTF_8)
        );

        //when
        doNothing().when(userService);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/user/fridge");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        ResultActions resultActions = this.mockMvc.perform(builder
                .file(mockMultipartFile)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("put-updateFridge",
                        requestHeaders(headerWithName("Access-Token").description("Jwt Access-Token")),
                        requestParts(partWithName("fridgeRequestDtoList").description(objectMapper.writeValueAsString(fridgeRequestDtoList)))
                ));
    }

}