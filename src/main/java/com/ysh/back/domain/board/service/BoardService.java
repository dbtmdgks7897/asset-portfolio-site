package com.ysh.back.domain.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.domain.board.dto.ResBoardListDTO;
import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.repository.BoardRepository;

@Service
public class BoardService {
    
    @Autowired
    BoardRepository boardRepository;

    // 게시물 리스트 뿌려주기 검색어, 정렬기준 입력 가능
    public ResponseEntity<ResponseDTO<Object>> getBoardListData(String search, String sort, Boolean desc){
        List<BoardEntity> boardEntityList;
        if((search != null) && (sort != null)){
            // 검색어, 정렬 기준 둘 다 입력
            if(desc.equals(true)){
                boardEntityList = boardRepository.findAllSearchedAndOrderBySortDesc(search, search, search, sort);
            } else {
                boardEntityList = boardRepository.findAllSearchedAndOrderBySort(search, search, search, sort);
            }
            
        } else if (search != null){
            // 검색어만 입력시
            boardEntityList = boardRepository.findByNameContainingOrContentContainingOrUserEntity_NameContaining(search, search, search);
        } else if (sort != null){
            // 정렬 기준만 입력 시
            if(desc.equals(true)){
                boardEntityList = boardRepository.findAllOrderBySortDesc(sort);
            }else{
                boardEntityList = boardRepository.findAllOrderBySort(sort);
            }
        } else {
            // 검색어, 정렬 기준 없음
            boardEntityList = boardRepository.findAll();
        }
        
        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 조회 성공")
            .data(ResBoardListDTO.of(boardEntityList))
            .build()   
        ,HttpStatus.OK);
    }

}
