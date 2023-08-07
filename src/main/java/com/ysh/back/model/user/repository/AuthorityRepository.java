package com.ysh.back.model.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.user.entity.AuthorityEntity;

@Repository
public interface AuthorityRepository  extends JpaRepository<AuthorityEntity, Integer>{
    Optional<AuthorityEntity> findByName(String name);
}
