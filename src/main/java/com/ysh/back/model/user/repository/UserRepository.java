package com.ysh.back.model.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.back.model.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByIdx(Long userIdx);

    Optional<UserEntity> findByEmail(String Email);
}
