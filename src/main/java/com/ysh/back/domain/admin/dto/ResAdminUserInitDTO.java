package com.ysh.back.domain.admin.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ysh.back.model.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResAdminUserInitDTO {

    private List<User> userList;

    public static ResAdminUserInitDTO of(List<UserEntity> userEntitiyList) {
        return ResAdminUserInitDTO.builder()
                .userList(
                        userEntitiyList.stream()
                                .map(userEntity -> User.fromEntity(userEntity))
                                .toList())
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class User {

        private Long idx;
        private Byte[] profileImg;
        private String imgType;
        private String email;
        private String name;
        private Integer age;
        private String gender;
        private String nickname;
        private String phone;
        private String createdAt;
        private LocalDateTime suspendUntil;
        private LocalDateTime deletedAt;

        public static User fromEntity(UserEntity userEntity) {
            return User.builder()
                    .idx(userEntity.getIdx())
                    // .profileImg(userEntity.getProfileImg())
                    .imgType(userEntity.getImgType())
                    .email(userEntity.getEmail())
                    .name(userEntity.getName())
                    .age(userEntity.getAge())
                    .gender(userEntity.getGender())
                    .nickname(userEntity.getNickname())
                    .phone(userEntity.getPhone())
                    .createdAt(userEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd"))))
                    .suspendUntil(userEntity.getSuspendUntil())
                    .deletedAt(userEntity.getDeletedAt())
                    .build();
        }
    }
}
