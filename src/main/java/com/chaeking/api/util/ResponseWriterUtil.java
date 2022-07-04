package com.chaeking.api.util;

import com.chaeking.api.config.WebConfig;
import com.chaeking.api.domain.value.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ResponseWriterUtil {

    public static void writeResponse(HttpServletResponse response, int statusCode, Object body) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setStatus(statusCode);
            response.getOutputStream().write(WebConfig.jsonMapper().writeValueAsBytes(body));
        } catch (IOException e) {
            log.error("response 작성 중 에러 발생" + e.getMessage());
        }
    }

    public static void writeBaseResponse(HttpServletResponse response, int statusCode, String message) {
        ResponseWriterUtil.writeResponse(response, statusCode, BaseResponse.of(message));
    }
}