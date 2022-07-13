package com.hanghae99_team3.model.good;

import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.model.board.BoardRepository;
import com.hanghae99_team3.model.user.UserController;
import com.hanghae99_team3.model.user.UserService;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.security.Principal;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class) // JUnit5에서 필요
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(GoodController.class)
@DisplayName("Good 컨트롤러 테스트")
class GoodControllerTest {
    MockMvc mockMvc;
    @MockBean
    UserController userController;
    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    BoardRepository boardRepository;

    @MockBean
    GoodRepositpory goodRepositpory;

    @MockBean
    GoodService goodService;


    final String accessToken = "JwtAccessToken";

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


    @Test
    @DisplayName("좋아요 생성")
    void createAndRemoveGood() throws Exception {
        //given


        //when
        doNothing().when(goodService);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/board/{boardId}/good", 1L)
                .header("Access-Token", accessToken)
                .principal(mockPrincipal));
        //then

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-good",
                        requestHeaders(
                                headerWithName("Access-Token").description("Jwt Access-Token"))));
    }
}


