package com.ysh.back.domain.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.repository.BoardRepository;

@Service
public class BoardService {
    
    @Autowired
    BoardRepository boardRepository;

    // 게시물 리스트 뿌려주기 검색어, 정렬기준 입력 가능
    public void getBoardListData(String search, String sort){
        List<BoardEntity> boardEntityList;
        if((search != null) && (sort != null)){
            // 검색어, 정렬 기준 둘 다 입력
            boardEntityList = null;
        } else if (search != null){
            // 검색어만 입력시
            boardEntityList = boardRepository.findByNameContainingOrContentContainingOrUserEntity_NameContaining(search, search, search);
        } else if (sort != null){
            // 정렬 기준만 입력 시
            boardEntityList = null;
        } else {
            // 검색어, 정렬 기준 없음
            boardEntityList = boardRepository.findAll();
        }
        
        System.out.println(boardEntityList.get(1).getName());

        
    }

}
