package com.ysh.back.domain.asset.stock.overseas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class OverseasServiceApiV1 {

    @Value("${external.alphavantage.apikey}")
    String apiKey;

    @Autowired
    UserRepository userRepository;

    public String getApikey(CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보가 존재하지 않습니다.");
        }

        return apiKey;
    }
}
