package com.ysh.back.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import com.ysh.back.config.security.auth.CustomAuthenticationFailureHandler;
import com.ysh.back.config.security.auth.CustomAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    // throws Exception : 오류를 함수 밖으로 내보내 바깥에서 오류 처리하도록 함
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // csrf 보안 해제 (실무에서는 보안 적용)
        httpSecurity.csrf(config -> config.disable());
       
        httpSecurity.authorizeHttpRequests(
            config -> config
            .requestMatchers(
                new MvcRequestMatcher(null, "/api/*/admin/**")
            )
            .hasRole("ADMIN")
            .requestMatchers(
                new MvcRequestMatcher(null, "/api/*/mypage/**")
            )
            .authenticated()
            .requestMatchers(
                new MvcRequestMatcher(null, "/css/**"),
                new MvcRequestMatcher(null, "/js/**"),
                new MvcRequestMatcher(null, "/img/**")
            )
            .permitAll()
            .requestMatchers(
                new MvcRequestMatcher(null, "/static/**")
            )
            .permitAll()
            .anyRequest()
            .permitAll()
        );
        // // 요청 주소 인증 및 인가 세팅
        // httpSecurity.authorizeHttpRequests(
        //     config -> config
        //     .requestMatchers("/css/**", "/js/**", "/img/**")
        //     // .requestMatchers(
        //     //     // null 자리에 뭐 넣어줘야됨 검색하셈
        //     //     new MvcRequestMatcher(null, "/css/**"),
        //     //     new MvcRequestMatcher(null, "/js/**"),
        //     //     new MvcRequestMatcher(null, "/img/**")
        //     // )
        //     // 위에 해당하는 주소의 요청은 인증 및 인가 없이 허용
        //     .permitAll()
        //     .requestMatchers("/auth/**", "/api/*/auth/**")
        //     .permitAll()
        //     .requestMatchers("/admin/**", "api/*/auth/**")
        //     // .hasRole("ADMIN")
        //     // 사용 시에는 무조건 ROLE_ 붙여서 사용해야 함
        //     // 위의 주소는 ADMIN 권한 필요
        //     .hasAuthority("ROLE_ADMIN")
        //     .anyRequest()
        //     // 위의 주소는 로그인(인증) 필요
        //     .authenticated()
        // );

        httpSecurity.formLogin(
            config -> config
            // 실제 로그인 컨트롤러 메소드 경로
            .loginPage("/auth/login")
            // 가상의 주소를 넣는다
            .loginProcessingUrl("/api/v1/auth/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .permitAll()
        );

        httpSecurity.logout(config -> config
                // 로그아웃 페이지 매핑
                .logoutUrl("/api/v1/auth/logout")
                // 세션 초기화(?)
                .invalidateHttpSession(true)
                // 쿠키 삭제
                .deleteCookies("JSESSIONID")
                // 로그아웃 성공 시 유저에게 보낼 내용
                .logoutSuccessHandler(logoutSuccessHandler)
                // 모든 유저 접근 허용
                .permitAll()
        );
        return httpSecurity.getOrBuild();
    }
}
