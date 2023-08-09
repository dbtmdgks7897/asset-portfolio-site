package com.ysh.back.model.user.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLES")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idx", callSuper = false)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, unique = true)
    private Integer idx;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roleEntityList")
    private List<UserEntity> userEntityList;

    @ManyToMany
    @JoinTable(name = "roles_authority",
               joinColumns = @JoinColumn(name = "roles_idx"),
               inverseJoinColumns = @JoinColumn(name = "authority_idx"))
    private List<AuthorityEntity> authorityEntityList;
}
