package com.ysh.back.domain.asset.bookmark.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ysh.back.model.bookmark.entity.BookmarkEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResGetBookmarkListDTO {
    private List<Bookmark> bookmarkList;

    public static ResGetBookmarkListDTO of(List<BookmarkEntity> entityList){
        return ResGetBookmarkListDTO.builder()
        .bookmarkList(
            entityList.stream()
            .map(entity -> Bookmark.fromEntity(entity))
            .toList()
        )
        .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class Bookmark{
        private String code;
        private String name;
        private BigDecimal price;
        private String type;

        public static Bookmark fromEntity(BookmarkEntity entity){
            return Bookmark.builder()
            .code(entity.getAssetEntity().getIdx())
            .name(entity.getAssetEntity().getName())
            .price(entity.getAssetEntity().getPrice())
            .type(entity.getAssetEntity().getAssetTypeEntity().getName())
            .build();
        }
    }
}
