package com.ysh.back.config.security.auth;

import java.io.IOException;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.back.common.dto.ResponseDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String message = "아이디와 비밀번호를 정확히 입력해주세요.";

        if (exception instanceof LockedException) {
            message = "계정이 잠겨있습니다.\n관리자에게 문의하여주십시오";
        } else if (exception instanceof AccountExpiredException) {
            message = "계정이 탈퇴되었습니다.\n관리자에게 문의하여주십시오";
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(objectMapper.writeValueAsString(
                ResponseDTO.builder()
                        .code(1)
                        .message(message)
                        .build()
        ));
    }

}
