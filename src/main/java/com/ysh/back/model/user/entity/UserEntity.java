package com.ysh.back.model.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.ysh.back.model.board.entity.BoardEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idx", callSuper = false)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, unique = true)
    private Long idx;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "phone")
    @Pattern(regexp = "regexp = \"/^\\\\d{3}-\\\\d{4}-\\\\d{4}$/\\r\\n" + //
            "\"")
    private String phone;

    // TODO : 이미지 업로드
    @Lob
    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "img_type")
    @ColumnDefault("png")
    private String imgType;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "suspend_until")
    private LocalDateTime suspendUntil;

    @Column(name = "suspend_reason")
    private String suspendReason;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_reason")
    private String deletedReason;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList;

    @ManyToMany
    @JoinTable(name = "user_roles",
               joinColumns = @JoinColumn(name = "user_idx"),
               inverseJoinColumns = @JoinColumn(name = "roles_idx"))
    private List<RoleEntity> roleEntityList;
}
