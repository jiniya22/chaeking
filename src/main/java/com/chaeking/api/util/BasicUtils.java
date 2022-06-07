package com.chaeking.api.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BasicUtils {

    public static Long getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object userId = request.getHeader("X-Chaeking-User-Id");
        if(userId instanceof Number n) {
            return n.longValue();
        }
        return null;
    }
}
