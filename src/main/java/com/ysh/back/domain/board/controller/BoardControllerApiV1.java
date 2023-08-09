package com.ysh.back.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.board.dto.ReqBoardPostDTO;
import com.ysh.back.domain.board.dto.ReqBoardRecommendDTO;
import com.ysh.back.domain.board.dto.ReqBoardReportDTO;
import com.ysh.back.domain.board.dto.ReqBoardUpdateDTO;
import com.ysh.back.domain.board.service.BoardServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "게시판", description = "게시판 관련")
@RestController
@RequestMapping("/api/v1/board")
public class BoardControllerApiV1 {

    @Autowired
    BoardServiceApiV1 boardServiceApiV1;

    @Operation(summary = "게시물 리스트", description = "전체 리스트를 반환하거나"
            + "search, sort, desc 받아 검색 / 정렬 / 내림차순 <br />"
            + "desc가 true면 내림차순")
    @GetMapping()
    public ResponseEntity<?> getBoardListData(
            // Get으로 세 개의 파라미터 받아옴
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Boolean desc) {
        desc = desc == null ? false : desc;
        return boardServiceApiV1.getBoardListData(search, sort, desc);
    }

    @Operation(summary = "게시물 상세 정보", description = "게시물 Idx를 받아와 상세 정보 반환")
    @GetMapping("/{boardIdx}")
    public ResponseEntity<?> getBoardDetailsData(
            @PathVariable Long boardIdx) {
        return boardServiceApiV1.getBoardDetailsData(boardIdx);
    }

    @Operation(summary = "게시물 작성", description = "게시물 name, content 받아와 게시물 작성")
    @PostMapping()
    public ResponseEntity<?> postBoardData(
            @Valid @RequestBody ReqBoardPostDTO reqBoardPostDTO) {
        System.out.println(reqBoardPostDTO);
        return boardServiceApiV1.postBoardData(reqBoardPostDTO);
    }

    @Operation(summary = "게시물 삭제", description = "게시물 작성자와 현재 사용자를 검사 후 삭제 기능<br />"
            + "검사 기능 추가 필요")
    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<?> deleteBoardData(
            @PathVariable Long boardIdx) {
        return boardServiceApiV1.deleteBoardData(boardIdx);
    }

    @Operation(summary = "게시물 댓글 조회", description = "현재 게시물의 idx로 댓글들 조회")
    @GetMapping("/{boardIdx}/comment")
    public ResponseEntity<?> getBoardCommentListData(
            @PathVariable Long boardIdx) {
        return boardServiceApiV1.getBoardCommentListData(boardIdx);
    }

    @Operation(summary = "게시물 신고", description = "신고 버튼 클릭 시 현재 유저가 누른 적 있는 지 판별<br />"
            + "없으면 신고 정보 board_report 테이블 등록")
    @PostMapping("/{boardIdx}/report")
    public ResponseEntity<?> insertBoardReportData(
            @PathVariable Long boardIdx,
            @RequestBody ReqBoardReportDTO reqBoardReportDTO) {
        return boardServiceApiV1.insertBoardReportData(boardIdx, reqBoardReportDTO);
    }

    @Operation(summary = "게시물 추천", description = "추천 버튼 클릭 시 현재 유저가 누른 적 있는 지 판별<br />"
            + "없으면 추천 정보 board_recommend 테이블 등록")
    @PostMapping("/{boardIdx}/recommend")
    public ResponseEntity<?> insertBoardRecommendData(
            @PathVariable Long boardIdx,
            @RequestBody ReqBoardRecommendDTO reqBoardRecommendDTO) {
        return boardServiceApiV1.insertBoardRecommendData(boardIdx, reqBoardRecommendDTO);
    }

    @Operation(summary = "게시물 수정 원본 데이터", description = "게시물 수정 페이지 진입 시 원래 게시물의 정보가 <br/>"
            + "title, content 인풋 박스에 들어있도록 함")
    @GetMapping("/{boardIdx}/update")
    public ResponseEntity<?> getBoardUpdateInitData(
            @PathVariable Long boardIdx) {
        return boardServiceApiV1.getBoardUpdateInitData(boardIdx);
    }

    @Operation(summary = "게시물 수정 요청", description = "게시물 내용 수정 후 업데이트")
    @PutMapping("/{boardIdx}/update")
    public ResponseEntity<?> updateBoardData(
            @PathVariable Long boardIdx,
            @Valid @RequestBody ReqBoardUpdateDTO reqBoardUpdateDTO) {
        return boardServiceApiV1.updateBoardData(boardIdx, reqBoardUpdateDTO);
    }
}
