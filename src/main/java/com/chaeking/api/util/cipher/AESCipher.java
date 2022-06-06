package com.chaeking.api.util.cipher;

import com.chaeking.api.config.exception.InvalidInputException;
import org.apache.logging.log4j.util.Strings;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCipher {
    static String ALGORITHM = "AES";
    static String TRANSFORMATION = ALGORITHM + "/CBC/PKCS5Padding";
    private static String PREFIX_SECRET_KEY = "chaeking";

    public static String encrypt(String data, String secretKey) {
        if(Strings.isBlank(data)) return data;
        if(Strings.isBlank(secretKey) || secretKey.length() < 32)
            throw new InvalidInputException("secret_key 는 최소 32 자 이상이어야 합니다.");

        try {
            SecretKeySpec key = new SecretKeySpec((PREFIX_SECRET_KEY + secretKey).substring(0,32).getBytes(), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(secretKey.substring(0, 16).getBytes());
            final Cipher c = Cipher.getInstance(TRANSFORMATION);
            c.init(Cipher.ENCRYPT_MODE, key, iv);
            final String encryptedData = new String(Base64.getEncoder().encode(c.doFinal(data.getBytes())), "UTF-8");
            return encryptedData;
        } catch (Exception e) {
            System.out.println(e);
            throw new InvalidInputException(String.format("AES 암호화 중 에러가 발생되었습니다. : %s", e.getMessage()));
        }
    }

    public static String decrypt(String data, String secretKey) {
        if(Strings.isBlank(data))
            throw new InvalidInputException("AES 복호화 대상은 null 또는 빈문자열을 허용하지 않습니다.");
        if(Strings.isBlank(secretKey) || secretKey.length() < 32)
            throw new InvalidInputException("secret_key 는 최소 32 자 이상이어야 합니다.");

        try {
            SecretKeySpec key = new SecretKeySpec((PREFIX_SECRET_KEY + secretKey).substring(0,32).getBytes(), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(secretKey.substring(0, 16).getBytes());
            final Cipher c = Cipher.getInstance(TRANSFORMATION);
            c.init(Cipher.DECRYPT_MODE, key, iv);
            final String decryptedData = new String(c.doFinal(Base64.getDecoder().decode(data.getBytes("UTF-8"))));
            return decryptedData;
        } catch (Exception e) {
            System.out.println(e);
            throw new InvalidInputException(String.format("AES 복호화 중 에러가 발생되었습니다. : %s", e.getMessage()));
        }
    }


}
