package com.chaeking.api.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class BasicUtils {

    public static Long getUserId() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        return Optional.ofNullable(response.getHeader("X-Chaeking-User-Id"))
                .map(Long::valueOf).orElse(null);
    }

    public static String getSimpleName(String name) {
        String simpleName = name.replaceAll("[^\\da-zA-Z가-힣]", "");
        return simpleName;
    }

}
