package com.hanghae99_team3.config;

import com.hanghae99_team3.login.exception.CustomAuthenticationEntryPoint;
import com.hanghae99_team3.login.handler.TokenAccessDeniedHandler;
import com.hanghae99_team3.login.jwt.JwtAuthFilter;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.handler.OAuth2SuccessHandler;
import com.hanghae99_team3.model.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
//    private final PrincipalOauth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://nyangjanggo.com");
        configuration.addAllowedOrigin("https://api.nyangjanggo.com");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PUT","DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) ->
                web
                        .ignoring()
                        .antMatchers("/h2-console/**");
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .cors().configurationSource(corsConfigurationSource())

                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.

                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    .accessDeniedHandler(tokenAccessDeniedHandler)

                // 권한 체크
                .and()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/api/boards/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/board/**").permitAll()
                    .antMatchers("/refresh/**").permitAll()
                    .antMatchers("/docs/**").permitAll()

                    //Test
                    .antMatchers("/api/subscribe/**").permitAll()
                    .antMatchers("/api/publish/**").permitAll()
                    .antMatchers("/sseTest.html").permitAll()
                    .antMatchers("/test/**").permitAll()
//                    .anyRequest().permitAll()
//                    .anyRequest().hasAnyRole("USER")
                    .antMatchers("/**").hasAnyRole("USER")


                .and()
                    .oauth2Login()
                    .successHandler(oAuth2SuccessHandler)


                .and()
                    .addFilterBefore(new JwtAuthFilter(jwtTokenProvider),
                            UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
        return http.build();
    }

}
