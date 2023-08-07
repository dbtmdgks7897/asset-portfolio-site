package com.ysh.back.domain.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReqJoinDTO {

    @Valid
    @NotNull(message = "회원 정보를 입력해주세요.")
    private User user;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class User {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")
        private String email;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotBlank(message = "이름을 입력해주세요")
        private String name;
        @Pattern( regexp = "/^\\d{3}-\\d{4}-\\d{4}$/\r\n")
        private String phone;
        @NotBlank(message = "닉네임을 입력해주세요")
        private String nickname;
    }
}


