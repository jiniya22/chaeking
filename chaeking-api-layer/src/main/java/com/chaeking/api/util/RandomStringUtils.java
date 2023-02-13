package com.chaeking.api.util;

import java.util.Random;

public class RandomStringUtils {

    private static final char[] RANDOM_CHARS =  { 'A', '1', '2', '3', '4', 'B', 'C', 'D', 'E', 'F', 'G', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '5', '6', '7', '8', '9', '0', 'H', 'I', 'J' };

    public static String generateRandomString(int num) {
        Random random = new Random(System.currentTimeMillis());
        int tablelength = RANDOM_CHARS.length;
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < num; i++) {
            sb.append(RANDOM_CHARS[random.nextInt(tablelength)]);
        }

        return sb.toString();
    }

}
