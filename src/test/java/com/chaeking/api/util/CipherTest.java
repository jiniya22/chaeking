package com.chaeking.api.util;

import com.chaeking.api.util.cipher.AESCipher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CipherTest {

    @DisplayName("1. AES 암호화 테스트")
    @Test
    void aesCipherTest() {
        String secretKey = "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB";
        String s1 = AESCipher.encrypt("jini", secretKey);
        System.out.println(s1);         // 99kOO+SV6fqcGIJVjVuBsA==

        String s2 = AESCipher.decrypt(s1, secretKey);
        System.out.println(s2);         // jini

        String s3 = AESCipher.encrypt(null, secretKey);
        System.out.println(s3);         // 99kOO+SV6fqcGIJVjVuBsA==
    }

}