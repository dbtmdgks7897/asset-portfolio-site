package com.ysh.back.model.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportEntityKey {
    private Long commentIdx;
    private Long userIdx;
}
