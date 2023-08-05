package com.ysh.back.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.board.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시판", description = "게시판 관련")
@RestController
@RequestMapping("/api/v1/board")
public class BoardControllerApiV1 {
    
    @Autowired
    BoardService boardService;

    @Operation(summary = "게시물 리스트",
    description = "전체 리스트를 출력하거나 search, sort 받아 검색 / 정렬")
    @GetMapping()
    public ResponseEntity<?> getBoardListData(
        // Get으로 두 개의 파라미터 받아옴
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String sort
        
    ){
        boardService.getBoardListData(search, sort);

        return ResponseEntity.ok().body(sort);
    }

}
