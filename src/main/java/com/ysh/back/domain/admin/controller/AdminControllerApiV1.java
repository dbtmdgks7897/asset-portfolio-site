package com.ysh.back.domain.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "관리 페이지", description = "유저 및 게시물 관리 페이지")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminControllerApiV1 {
    
}
