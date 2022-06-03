package com.chaeking.api.util.cipher;

import com.chaeking.api.config.exception.ServerErrorException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Cipher {
    static String ALGORITHM = "SHA-256";

    public static String getEncSHA256(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance(ALGORITHM);
            mDigest.update(input.getBytes());
            return bytesToHex(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ServerErrorException(String.format("현재 서버에서 %s 알고리즘을 지원하지 않습니다.", ALGORITHM));
        }
    }

    static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
