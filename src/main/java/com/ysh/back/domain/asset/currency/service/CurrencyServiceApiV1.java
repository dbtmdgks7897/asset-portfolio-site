package com.ysh.back.domain.asset.currency.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ysh.back.domain.asset.bitcoin.dto.ResGetBitcoinDetailDTO;

@Service
public class CurrencyServiceApiV1 {
    
    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyServiceApiV1(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal updatePrice(String currencyCode){
        String[] split = currencyCode.split("/");
        String url = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"
        + split[1].toLowerCase() + "/" + split[0].toLowerCase() +".json";


        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        BigDecimal one = new BigDecimal(1);
        BigDecimal cur = new BigDecimal(response.getBody().split(",")[1].split(":")[1].replaceAll(" ", "").replace("}", "").trim());
        BigDecimal price = one.divide(cur, 2, RoundingMode.HALF_UP);

        return price;
    }

}
