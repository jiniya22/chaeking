package com.chaeking.api.util;

import lombok.Builder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

public class JsoupTest {

    final UriComponents base = UriComponentsBuilder.fromHttpUrl("https://search.daum.net/search?w=bookpage&bookId=356183&q=소망").build();

    @DisplayName("1. Jsoup 테스트")
    @Test
    void test1() throws IOException {
        Document document = Jsoup.connect(base.toUriString()).get();
        List<String> tt = document.select("#tabContent > div:nth-child(1) > div.info_section.info_intro > div.wrap_cont > dl:nth-child(3) > dd > a").eachAttr("href");
        tt.forEach(System.out::println);
    }
/*
?w=book&q=%EA%B9%80%EC%84%B1%EC%B6%98&DA=LB0&target=person
?w=book&q=%EC%8B%A0%EC%84%A0%EC%9B%85&DA=LB0&target=person
 */

    @DisplayName("2. ")
    @Test
    void test2() {
        String httpUrl = "https://api.chaeking.com?w=bookpage&bookId=356183&q=%EC%86%8C%EB%A7%9D";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(httpUrl).build();
        uri.getQueryParams().entrySet().forEach(System.out::println);
    }

    @DisplayName("3. ")
    @Test
    void test3() throws UnsupportedEncodingException {
        String httpUrl = "https://api.chaeking.com?w=bookpage&bookId=356183&q=%EC%86%8C%EB%A7%9D";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(httpUrl).build();
        MultiValueMap<String, String> queryParams = uri.getQueryParams();
        if(queryParams.containsKey("bookId"))
            System.out.println(queryParams.get("bookId"));
        if(queryParams.containsKey("q"))
            System.out.println(URLDecoder.decode(queryParams.get("q").get(0), "UTF-8"));
    }

    @DisplayName("4. ")
    @Test
    void test4() {
        System.out.println(getAuthor("?w=book&q=%EA%B9%80%EC%84%B1%EC%B6%98&DA=LB0&target=person"));
        System.out.println(getAuthor("?w=book&q=%EC%8B%A0%EC%84%A0%EC%9B%85&DA=LB0&target=person"));
    }

    Author getAuthor(String queryString) {
        queryString = queryString.replaceAll("^.*\\?", "");
        Author.AuthorBuilder authorBuilder = Author.builder();

        Arrays.stream(queryString.split("&")).forEach(query -> {
            String[] q = query.split("=");
            if(q.length >= 2) {
                switch (q[0]) {
                    case "q" -> authorBuilder.name(q[1]);
                    case "bookId" -> authorBuilder.bookId(Long.valueOf(q[1]));
                }
            }
        });

        return authorBuilder.build();
    }

    @Builder
    record Author(long bookId, String name) {
    }
}
