package com.ysh.back.domain.asset.stock.apiauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResAccessTokenDTO {
    private String access_token;
    private String token_type;
    private int expires_in;
}
