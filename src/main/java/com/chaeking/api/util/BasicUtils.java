package com.chaeking.api.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicUtils {

    public static Long getUserId() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Object userId = response.getHeader("X-Chaeking-User-Id");
        if(userId instanceof Number n) {
            return n.longValue();
        } else if(userId instanceof String s) {
            return Long.valueOf(s);
        }
        return null;
    }
}
