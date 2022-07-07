package com.hanghae99_team3.model.user;

import com.hanghae99_team3.model.user.domain.AuthProvider;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.security.MockSpringSecurityFilter;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(RestDocumentationExtension.class) // JUnit5에서 필요
@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    // MockMvc, Spring Rest Docs Setup
    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();

    }


    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    private final String accessToken = "JwtAccessToken";
    private Principal mockPrincipal;
    private User testUser;
    private void mockUserSetup() {
        User testUser = User.testRegister()
                .email("email@test.com")
                .password("password")
                .userImg("userImgLink")
                .provider(AuthProvider.kakao)
                .providerId("providerId")
                .nickname("nickname")
                .role(UserRole.USER)
                .userDescription("description")
                .build();

        this.testUser = testUser;
        PrincipalDetails testUserDetails = new PrincipalDetails(testUser);
        this.mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("회원정보 조회")
    void getUser() throws Exception {
        //given
        this.mockUserSetup();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));

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

    @Test
    @DisplayName("회원정보 수정")
    void updateUser() throws Exception {
        //given
        this.mockUserSetup();
        MockMultipartFile image = new MockMultipartFile(
                "userImg",
                "userImg.png",
                "image/png",
                "<<png data>>".getBytes(StandardCharsets.UTF_8));

        //when
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/user");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        ResultActions resultActions = this.mockMvc.perform(builder
                .file(image)
                        .param("nickname", "nickname")
                        .param("userDescription", "userDescription")
                .header("Access-Token", accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .principal(mockPrincipal));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("put-user",
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token")
                        ),
                        requestParameters(
                                parameterWithName("nickname").description("변경할 닉네임"),
                                parameterWithName("userDescription").description("변경할 유저 소개글")
                        ),
                        requestParts(
                                partWithName("userImg").description("변경할 프로필 이미지 ")
                        )
                ));

    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser() throws Exception {
        //given
        this.mockUserSetup();

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


}