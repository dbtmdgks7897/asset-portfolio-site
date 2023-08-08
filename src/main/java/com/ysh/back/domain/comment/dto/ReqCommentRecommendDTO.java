package com.ysh.back.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReqCommentRecommendDTO {
    private Long userIdx;
    private Long boardIdx;
}
