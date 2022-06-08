package com.chaeking.api.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

public class BasicUtils {

    public static Long getUserId() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        String userId = response.getHeader("X-Chaeking-User-Id");
        return Long.valueOf(userId);
    }
}
