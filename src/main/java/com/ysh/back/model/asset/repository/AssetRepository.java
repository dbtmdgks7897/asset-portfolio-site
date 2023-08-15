package com.ysh.back.model.asset.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.asset.entity.AssetEntity;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Integer>{
    Optional<AssetEntity> findByIdx(String idx);
}
