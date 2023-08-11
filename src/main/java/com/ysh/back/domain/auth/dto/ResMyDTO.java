package com.ysh.back.domain.auth.dto;

import java.util.List;

import com.ysh.back.model.user.entity.RoleEntity;
import com.ysh.back.model.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResMyDTO {
    private Long idx;
    private String nickname;
    private String profileImg;
    private List<Role> roleList;

    public static ResMyDTO fromEntity(UserEntity userEntity){
        return ResMyDTO.builder()
        .idx(userEntity.getIdx())
        .nickname(userEntity.getNickname())
        .profileImg(userEntity.getProfileImg())
        .roleList(
            userEntity.getRoleEntityList().stream()
            .map(roleEntity -> Role.fromEntity(roleEntity))
            .toList()
        )
        .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class Role{
        private Integer idx;
        private String name;

        public static Role fromEntity(RoleEntity roleEntity){
            return Role.builder()
            .idx(roleEntity.getIdx())
            .name(roleEntity.getName())
            .build();
        }
    }
}
