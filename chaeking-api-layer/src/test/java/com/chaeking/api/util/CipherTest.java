package com.chaeking.api.util;

import com.chaeking.api.util.cipher.AESCipher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

class CipherTest {

    @DisplayName("1. AES 암호화 테스트")
    @Test
    void aesCipherTest() {
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        String secretKey = "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB";
        String password = AESCipher.encrypt("CX34668FVC", secretKey);
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
        System.out.println(System.getenv("X-Naver-Client-Secret"));
    }
}