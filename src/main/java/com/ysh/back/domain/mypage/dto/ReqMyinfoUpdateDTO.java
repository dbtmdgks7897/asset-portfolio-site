package com.ysh.back.domain.mypage.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReqMyinfoUpdateDTO {
    // private Long idx;
    // private Byte[] profileImg;
    // private MultipartFile profileImg;
    // private String imgType;
    private MultipartFile file;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;
    private String gender;
    private Integer age;
    @Pattern( regexp = "/^\\d{3}-\\d{4}-\\d{4}$/\r\n")
    private String phone;

}
