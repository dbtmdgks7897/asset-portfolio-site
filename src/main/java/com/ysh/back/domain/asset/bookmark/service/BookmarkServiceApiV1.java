package com.ysh.back.domain.asset.bookmark.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.bookmark.dto.ReqPostBookmarkDTO;
import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.repository.AssetRepository;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.bookmark.entity.BookmarkEntity;
import com.ysh.back.model.bookmark.repository.BookmarkRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BookmarkServiceApiV1 {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    AuditLogRepository auditLogRepository;


    public boolean isBookmarked(String stockCode, CustomUserDetails customUserDetails){
        Optional<BookmarkEntity> bookmarkEntityOptional = bookmarkRepository.findByUserEntityIdxAndAssetEntityIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx(), stockCode);
        if(bookmarkEntityOptional.isPresent()){
            return true;
        }else{
            return false;
        }

    }

    @Transactional
    public ResponseEntity<?> postBookmarkData(ReqPostBookmarkDTO reqPostBookmarkDTO,
            CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();
        
        Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqPostBookmarkDTO.getAssetIdx());
        if (!assetEntityOptional.isPresent()) {
            throw new BadRequestException("해당 자산 정보를 찾을 수 없습니다.");
        }
        AssetEntity assetEntity = assetEntityOptional.get();

        Optional<BookmarkEntity> bookmarkEntityOptional = bookmarkRepository
                .findByUserEntityIdxAndAssetEntityIdx(userEntity.getIdx(), reqPostBookmarkDTO.getAssetIdx());
        if (bookmarkEntityOptional.isPresent()) {
            BookmarkEntity bookmarkEntity = bookmarkEntityOptional.get();
            AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("bookmark")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(bookmarkEntity.getIdx()))
                    .operation("DELETE")
                    .oldValue(bookmarkEntity.getUserEntity().getIdx() + ":" + bookmarkEntity.getAssetEntity().getName())
                    .reason("즐겨찾기 삭제")
                    .build();

            bookmarkRepository.delete(bookmarkEntity);
            auditLogRepository.save(auditLog);

            return new ResponseEntity<>(
                    ResponseDTO.builder()
                            .code(0)
                            .message("즐겨찾기를 삭제하였습니다.")
                            .build(),
                    HttpStatus.OK);
        } else {
            BookmarkEntity bookmarkEntityForSaving = BookmarkEntity.builder()
                    .userEntity(userEntity)
                    .assetEntity(assetEntity)
                    .build();

            BookmarkEntity bookmarkEntity = bookmarkRepository.save(bookmarkEntityForSaving);

            AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("bookmark")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(bookmarkEntity.getIdx()))
                    .operation("INSERT")
                    .oldValue(bookmarkEntity.getUserEntity().getIdx() + ":" + bookmarkEntity.getAssetEntity().getName())
                    .reason("즐겨찾기 추가")
                    .build();

            auditLogRepository.save(auditLog);

            return new ResponseEntity<>(
                    ResponseDTO.builder()
                            .code(0)
                            .message("즐겨찾기를 추가하였습니다.")
                            .build(),
                    HttpStatus.OK);
        }

    }

}
