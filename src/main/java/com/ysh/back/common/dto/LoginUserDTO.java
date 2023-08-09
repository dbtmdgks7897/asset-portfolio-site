package com.ysh.back.common.dto;

import com.ysh.back.model.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LoginUserDTO {

    private User user;

    public static LoginUserDTO of(UserEntity userEntity) {
        return LoginUserDTO.builder()
                .user(User.fromEntity(userEntity))
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class User {
        private Long idx;
        private String email;
        private String password;
        private List<String> roleList;

        public static User fromEntity(UserEntity userEntity) {
            return User.builder()
                    .idx(userEntity.getIdx())
                    .email(userEntity.getEmail())
                    .password(userEntity.getPassword())
                    .roleList(
                            userEntity.getRoleEntityList()
                                    .stream()
                                    .map(userRoleEntity -> userRoleEntity.getName())
                                    .toList()
                    )
                    .build();
        }
    }
}
