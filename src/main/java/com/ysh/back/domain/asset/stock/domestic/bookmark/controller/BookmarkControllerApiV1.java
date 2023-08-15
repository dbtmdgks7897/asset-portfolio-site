package com.ysh.back.domain.asset.stock.domestic.bookmark.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.asset.stock.domestic.bookmark.service.BookmarkServiceApiV1;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class BookmarkControllerApiV1 {

    @Autowired
    BookmarkServiceApiV1 bookmarkServiceApiV1;

    // public ResponseEntity<?> postBookmarkData(
    //     @Valid @RequestBody 
    // ){

    // }
}
