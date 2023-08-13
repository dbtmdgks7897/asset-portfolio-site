package com.ysh.back.domain.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.dto.ReqPortfolioInsertDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;

@Service
public class PortfolioServiceApiV1 {
    
    @Autowired
    AuditLogEntity auditLogEntity;

    public ResponseEntity insertPortfolio(ReqPortfolioInsertDTO reqPortfolioInsertDTO, CustomUserDetails customUserDetails){
        
    }

}
