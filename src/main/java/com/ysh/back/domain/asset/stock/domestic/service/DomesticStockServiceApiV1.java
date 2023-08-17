package com.ysh.back.domain.asset.stock.domestic.service;

import java.net.URI;
import java.util.Optional;

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

import com.ysh.back.common.apiauth.service.ApiAuthServiceApiV1;
import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.dto.ApiGetDomesticStockNameDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.service.AssetServiceApiV1;
import com.ysh.back.domain.asset.stock.domestic.dto.ReqGetDomesticStockInfoDTO;
import com.ysh.back.domain.asset.stock.domestic.dto.ResDomesticStockInfoDTO;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class DomesticStockServiceApiV1 {

    @Value("${external.api.baseurl}")
    private String apiBaseUrl;

    @Value("${external.api.appkey}")
    private String appKey;

    @Value("${external.api.appsecretkey}")
    private String appSecretKey;

    @Value("${external.datago.baseurl}")
    private String datagoBaseUrl;

    @Value("${external.datago.servicekey}")
    private String datagoServiceKey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApiAuthServiceApiV1 apiAuthServiceApiV1;

    @Autowired
    AssetServiceApiV1 assetServiceApiV1;

    public ResponseEntity<?> getDomesticStockInfoData(String stockCode, CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        if (stockCode.length() != 6) {
            throw new BadRequestException("정확한 종목 '코드'를 입력해주세요.");
        }

        String path = "/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=J&fid_input_iscd="
                + stockCode;
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

        ResDomesticStockInfoDTO responseBody = response.getBody();

        if (responseBody.getOutput().getBstp_kor_isnm().equals(" ")) {
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

    public ResponseEntity<?> getDomesticStockDetailData(String stockCode, String stockType, CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        System.out.println(stockCode);
        String path = "/getStockPriceInfo?serviceKey=" + datagoServiceKey
                + "&numOfRows=1&pageNo=1&resultType=json&likeSrtnCd=" + stockCode;
        String url = datagoBaseUrl + path;

        URI uri = null;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            System.out.println(e);
        }

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ApiGetDomesticStockNameDTO> response = restTemplate.getForEntity(uri,
                ApiGetDomesticStockNameDTO.class);

        ApiGetDomesticStockNameDTO responseBody = response.getBody();

        System.out.println(responseBody.getResponse().getHeader().getResultMsg());

        if (!responseBody.getResponse().getHeader().getResultCode().equals("00")) {
            throw new BadRequestException("주식 이름 불러오기 오류");
        }

        ReqPostAssetDTO dto = ReqPostAssetDTO.builder()
        .assetCode(stockCode)
        .assetName(responseBody.getResponse().getBody().getItems().getItem().get(0).getItmsNm())
        .assetType(stockType)
        .build();

        // TODO : 나중에 웹소켓? 연결하고 데이터 더 줘야되는데 이러면 여기서는 이거만 주면 되나?
        return assetServiceApiV1.postAssetData(dto, customUserDetails);
    }
}
