package com.ysh.back.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.board.service.BoardServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시판", description = "게시판 관련")
@RestController
@RequestMapping("/api/v1/board")
public class BoardControllerApiV1 {
    
    @Autowired
    BoardServiceApiV1 boardService;

    @Operation(summary = "게시물 리스트",
    description = "전체 리스트를 반환하거나"
    + "search, sort, desc 받아 검색 / 정렬 / 내림차순 <br />"
    + "desc가 true면 내림차순")
    @GetMapping()
    public ResponseEntity<?> getBoardListData(
        // Get으로 세 개의 파라미터 받아옴
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean desc
    ){
        desc = desc == null ? false : desc;
                return boardService.getBoardListData(search, sort, desc);
    }

    @Operation(summary = "게시물 상세 정보",
    description = "게시물 Idx를 받아와 상세 정보 반환")
    @GetMapping("/{boardIdx}")
    public ResponseEntity<?> getBoardDetailsData(
        @PathVariable Long boardIdx
    ) {
        return boardService.getBoardDetailsData(boardIdx);
    }

    @Operation(summary = "게시물 삭제",
    description = "게시물 작성자와 현재 사용자를 검사 후 삭제 기능<br />"
    + "검사 기능 추가 필요")
    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<?> deleteBoardData(
        @PathVariable Long boardIdx
    ) {
        return boardService.deleteBoardData(boardIdx);
    }

    @Operation(summary = "게시물 댓글",
    description = "게시물 댓글 조회 기능")
    @GetMapping("/{boardIdx}/comment")
    public ResponseEntity<?> getBoardCommentListData(
        @PathVariable Long boardIdx
    ) {
        return boardService.getBoardCommentListData(boardIdx);
    }
}
