package com.ysh.back.model.asset.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.asset.entity.AssetTypeEntity;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetTypeEntity, Integer>{
    Optional<AssetTypeEntity> findByIdx(Integer typeIdx);
    Optional<AssetTypeEntity> findByName(String typeName);

}
