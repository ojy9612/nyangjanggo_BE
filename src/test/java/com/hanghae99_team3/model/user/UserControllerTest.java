//package com.hanghae99_team3.model.user;
//
//import com.hanghae99_team3.model.user.domain.AuthProvider;
//import com.hanghae99_team3.model.user.domain.User;
//import com.hanghae99_team3.model.user.domain.UserRole;
//import com.hanghae99_team3.model.user.repository.UserRepository;
//import com.hanghae99_team3.security.jwt.JwtTokenProvider;
//import com.hanghae99_team3.security.jwt.TokenDto;
//import com.hanghae99_team3.security.oauth2.PrincipalDetails;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//
//import javax.annotation.PostConstruct;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(RestDocumentationExtension.class)
//@SpringBootTest()
//class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp(WebApplicationContext webApplicationContext,
//                      RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @MockBean
//    private UserRepository userRepository;
//
////    @MockBean
////    private UserService userService;
//
//
//    private String accessToken;
//
//    @BeforeEach
//    public void getAccessToken() throws Exception {
//
//        User testUser = User.oauth2Register()
//                .email("email@test.com")
//                .password("password")
//                .userImg("userImgLink")
//                .provider(AuthProvider.kakao)
//                .providerId("providerId")
//                .nickname("nickname")
//                .role(UserRole.USER)
//                .build();
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
//        PrincipalDetails principalDetails = new PrincipalDetails(testUser);
//        TokenDto token = jwtTokenProvider.createToken(principalDetails.getUsername(), principalDetails.getRole());
//        this.accessToken = token.getAccessToken();
//        System.out.println("token = " + token);
//    }
//
//
//    @PostConstruct
//    public void settingUserTest() {
//        User testUser = User.oauth2Register()
//                .email("email@test.com")
//                .password("password")
//                .userImg("userImgLink")
//                .provider(AuthProvider.kakao)
//                .providerId("providerId")
//                .nickname("nickname")
//                .role(UserRole.USER)
//                .build();
//        userRepository.save(testUser);
//    }
//
//    @Test
//    @WithUserDetails(value = "email@test.com")
////    @WithUserDetails("email@test.com")
//    @DisplayName("회원정보 조회")
//    void getUser() throws Exception {
//        System.out.println("accessToken = " + accessToken);
//        this.mockMvc.perform(get("/api/user")
//                        .header("Access-Token", accessToken))
//
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
////    private String getAccessToken() throws Exception {
////        String clientId = "foo";
////        String clientSecret = "bar";
////        String username = "id";
////        String password = "password";
////
////        ResultActions perform = this.mvc.perform(post("/oauth/token")
////                .with(httpBasic(clientId, clientSecret))
////                .param("username", username)
////                .param("password", password)
////                .param("grant_type", "password"));
////
////        return (String) new Jackson2JsonParser().parseMap(perform.andReturn().getResponse().getContentAsString()).get("access_token");
////    }
//
//}