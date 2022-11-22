package com.chaeking.api.config;

import com.chaeking.api.config.vault.BookSearchConfig;
import com.chaeking.api.config.vault.ChaekingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VaultConfigTest {

    @Test
    void test1() {
        System.out.println(">>>>");
        System.out.println(BookSearchConfig.Naver.getClientId());
        System.out.println(ChaekingConfig.getSecret());
        System.out.println(ChaekingConfig.Data4library.getAuthKey());
        System.out.println(">>>>>");
    }
}
