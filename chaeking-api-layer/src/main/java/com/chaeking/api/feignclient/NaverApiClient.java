package com.chaeking.api.feignclient;

import com.chaeking.api.model.naver.NaverBookValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "naverApi", url = "${chaeking.book-search.naver.api-url}")
public interface NaverApiClient {

    @GetMapping("/v1/search/book.json")
    ResponseEntity<NaverBookValue.Res.BookBasic> searchBooks(
            @RequestHeader("X-Naver-Client-Id") String clientId,
            @RequestHeader("X-Naver-Client-Secret") String clientSecret,
            @SpringQueryMap NaverBookValue.Req.Search naverBookSearch);

}
