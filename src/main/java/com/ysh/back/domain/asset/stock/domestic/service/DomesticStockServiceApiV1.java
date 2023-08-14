package com.ysh.back.domain.asset.stock.domestic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.domain.asset.stock.apiauth.service.ApiAuthServiceApiV1;
import com.ysh.back.domain.asset.stock.domestic.dto.ReqGetDomesticStockInfoDTO;
import com.ysh.back.domain.asset.stock.domestic.dto.ResDomesticStockInfoDTO;

@Service
public class DomesticStockServiceApiV1 {

    @Value("${external.api.baseurl}")
    private String apiBaseUrl;

    @Value("${external.api.appkey}")
    private String appKey;

    @Value("${external.api.appsecretkey}")
    private String appSecretKey;

    @Autowired
    ApiAuthServiceApiV1 apiAuthServiceApiV1;

    public ResponseEntity<?> getStockInfoData(String stockCode) {
        if(stockCode.length() != 6){
            throw new BadRequestException("정확한 종목 '코드'를 입력해주세요.");
        }

        String path = "/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=J&fid_input_iscd="+stockCode;
        String url = apiBaseUrl + path;

        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiAuthServiceApiV1.getAccessToken());
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecretKey);
        headers.set("tr_id", "FHKST01010100");
        // ... 필요한 헤더 추가 ...


        HttpEntity<ReqGetDomesticStockInfoDTO> entity = new HttpEntity<>(headers);

        ResponseEntity<ResDomesticStockInfoDTO> response = restTemplate.exchange(url, HttpMethod.GET,
        entity, ResDomesticStockInfoDTO.class);


        // try {
        //     ResponseEntity<ResAccessTokenDTO> response = restTemplate.postForEntity(url, entity,
        //             ResAccessTokenDTO.class);
        //     ResAccessTokenDTO responseBody = response.getBody();
        //     System.out.println("Response Body: " + responseBody);
        // } catch (HttpServerErrorException.InternalServerError e) {
        //     String responseBody = e.getResponseBodyAsString();
        //     System.out.println("Error Response Body: " + responseBody);
        // }
        System.out.println(response.getStatusCode());
        System.out.println(response.getClass());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
        ResDomesticStockInfoDTO responseBody = response.getBody();

        if(responseBody.getOutput() == null){
            throw new BadRequestException("해당 종목이 존재하지 않습니다.");
        }

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(Integer.parseInt(responseBody.getRtCd()))
                        .message(responseBody.getMsg1())
                        .data(responseBody.getOutput())
                        .build(),
                HttpStatus.OK);
    }
}
