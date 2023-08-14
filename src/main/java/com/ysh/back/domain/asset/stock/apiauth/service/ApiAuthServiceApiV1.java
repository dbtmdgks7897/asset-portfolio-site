package com.ysh.back.domain.asset.stock.apiauth.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ysh.back.domain.asset.stock.apiauth.dto.ResAccessTokenDTO;

@Service
public class ApiAuthServiceApiV1 {
    @Value("${external.api.baseurl}")
    private String apiBaseUrl;

    @Value("${external.api.appkey}")
    private String appKey;

    @Value("${external.api.appsecretkey}")
    private String appSecretKey;

    private String path = "/oauth2/tokenP";

    @Cacheable(value = "accessTokenCache", key = "'access_token'")
    public String getAccessToken() {
        String url = apiBaseUrl + path;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("appkey", appKey);
        requestBody.put("appsecret", appSecretKey);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<ResAccessTokenDTO> response = restTemplate.postForEntity(url, entity, ResAccessTokenDTO.class);

        // ResAccessTokenDTO accessTokenDTO = response.getBody();
        // if (accessTokenDTO != null) {
        // return accessTokenDTO.getAccess_token();
        // } else {
        // // 응답이 올바르지 않은 경우 예외 처리 또는 로그 남기기
        // return null;
        // }
        System.out.println("접근 토큰 : " + response.getBody().getAccess_token());
        return response.getBody().getAccess_token();
    }
}
