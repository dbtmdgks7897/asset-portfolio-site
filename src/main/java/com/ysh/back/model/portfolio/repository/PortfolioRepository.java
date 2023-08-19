package com.ysh.back.model.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.portfolio.entity.PortfolioEntity;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Integer>{
    Optional<PortfolioEntity> findByName(String name);   
    Optional<PortfolioEntity> findByIdx(Integer Idx);   

    List<PortfolioEntity> findByUserEntityIdxAndDeletedAtIsNull(Long userIdx);
}
