package com.chaeking.api.util;

import com.chaeking.api.util.cipher.AESCipher;
import com.chaeking.api.util.cipher.SHA256Cipher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CipherTest {

    @DisplayName("1. AES 암호화 테스트")
    @Test
    void aesCipherTest() {
        String secretKey = "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB";
        String password = AESCipher.encrypt("222", secretKey);
        System.out.println(password);         // 99kOO+SV6fqcGIJVjVuBsA==

        String s2 = AESCipher.decrypt(password, secretKey);
        System.out.println(s2);         // jini

        System.out.println(SHA256Cipher.convertHash(s2));
        System.out.println(SHA256Cipher.convertHash(s2));
        System.out.println(SHA256Cipher.convertHash(s2));
    }

}