package com.ysh.back.model.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.transaction.entity.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer>{
    List<TransactionEntity> findByPortfolioEntityIdx(Integer portfolioIdx);
}
