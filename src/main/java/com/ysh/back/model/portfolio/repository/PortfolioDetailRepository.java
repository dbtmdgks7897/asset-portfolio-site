package com.ysh.back.model.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;

@Repository
public interface PortfolioDetailRepository extends JpaRepository<PortfolioDetailEntity, Long>{
    Optional<PortfolioDetailEntity> findByPortfolioEntityIdxAndAssetEntityIdx(Integer portfolioIdx, String assetIdx);
    List<PortfolioDetailEntity> findByPortfolioEntityIdx(Integer idx);
}
