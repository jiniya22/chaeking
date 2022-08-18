package com.chaeking.api.feignclient;

import com.chaeking.api.domain.value.naver.KakaoBookValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoApi", url = "${chaeking.book-search.kakao.api-url}")
public interface KakaoApiClient {

    @GetMapping("/v3/search/book")
    ResponseEntity<KakaoBookValue.Res.BookBasic> searchBooks(
            @RequestHeader("Authorization") String authorization,
            @SpringQueryMap KakaoBookValue.Req.Search kakaoBookSearch);

}
