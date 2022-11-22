package com.chaeking.api.config.vault;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("book-search")
public class BookSearchConfig {
    private static Naver naver;
    private static Kakao kakao;

    public static class Naver {
        @Getter private static String clientId;
        @Getter private static String clientSecret;

        public void setClientId(String clientId) { if (Naver.clientId == null) Naver.clientId = clientId; }
        public void setClientSecret(String clientSecret) { if (Naver.clientSecret == null) Naver.clientSecret = clientSecret; }
    }

    public static class Kakao {
        @Getter private static String apiKey;

        public void setApiKey(String apiKey) { if(Kakao.apiKey == null) Kakao.apiKey = apiKey; }
    }

    public void setKakao(Kakao kakao) { if(BookSearchConfig.kakao == null) BookSearchConfig.kakao = kakao; }
    public void setNaver(Naver naver) { if(BookSearchConfig.naver == null) BookSearchConfig.naver = naver; }
}
