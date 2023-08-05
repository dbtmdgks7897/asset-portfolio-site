package com.ysh.back.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.back.model.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    
}
