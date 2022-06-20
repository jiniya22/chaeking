package com.chaeking.api.util;

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
}
