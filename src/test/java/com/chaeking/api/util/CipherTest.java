package com.chaeking.api.util;

import com.chaeking.api.util.cipher.AESCipher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class CipherTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("1. AES 암호화 테스트")
    @Test
    void aesCipherTest() {
        String secretKey = "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB";
        String password = AESCipher.encrypt("222", secretKey);
        System.out.println(password);         // 99kOO+SV6fqcGIJVjVuBsA==

        String s2 = AESCipher.decrypt(password, secretKey);
        System.out.println(s2);         // jini

        String hash1 = passwordEncoder.encode(s2);
        String hash2 = passwordEncoder.encode(s2);
        String hash3 = passwordEncoder.encode(s2);

        System.out.println(hash1);
        System.out.println(passwordEncoder.matches(s2, hash1));
        System.out.println(passwordEncoder.matches(s2, hash2));
        System.out.println(passwordEncoder.matches(s2, hash3));
    }

    @DisplayName("1. ")
    @Test
    void test1() {
        Object tt = 33.652;
        if(tt instanceof Number n) {
            Long l = n.longValue();
            System.out.println(l);
        }
    }
}