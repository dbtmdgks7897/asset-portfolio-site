package com.ysh.back.model.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;

@Repository
public interface PortfolioDetailRepository extends JpaRepository<PortfolioDetailEntity, Long>{
    
}