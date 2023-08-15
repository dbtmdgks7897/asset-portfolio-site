package com.ysh.back.common.apiauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.common.apiauth.service.ApiAuthServiceApiV1;

@RestController
@RequestMapping("/api/v1/apiAuth")
public class ApiAuthControllerApiV1 {
    
    @Autowired
    ApiAuthServiceApiV1 apiAuthServiceApiV1;

    @PostMapping("/approval")
    public String getApprovalKey(){
        return apiAuthServiceApiV1.getApprovalKey();
    }
}
