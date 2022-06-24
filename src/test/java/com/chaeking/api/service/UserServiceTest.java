package com.chaeking.api.service;

import com.chaeking.api.domain.value.UserValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("1. user save test")
    @Test
    void test1() {
        userService.save(UserValue.Req.Creation.builder().nickname("솔").email("sol@applebox.xyz")
                .sex("M")
                .password("3wqPrWol1T3h/3U+w2abGw==").secretKey("A37aXdxH6gwTySajLe8eZWNvyC2yuZVB").build());
    }
}