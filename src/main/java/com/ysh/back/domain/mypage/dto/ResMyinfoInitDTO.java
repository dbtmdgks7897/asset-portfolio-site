package com.ysh.back.domain.mypage.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResMyinfoInitDTO {
    private Byte[] profileImg;
    private String imgType;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String phone;
    private Integer age;
}
