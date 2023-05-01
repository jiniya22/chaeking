package com.chaeking.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Slf4j
@Configuration
public class LoggingFilterConfig {
    private static boolean COMMONS_REQUEST_LOGGING_FILTER_FLAG;

    private CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
        if(COMMONS_REQUEST_LOGGING_FILTER_FLAG) {
            commonsRequestLoggingFilter.setIncludeClientInfo(true);
            commonsRequestLoggingFilter.setIncludeQueryString(true);
            commonsRequestLoggingFilter.setIncludePayload(true);
            commonsRequestLoggingFilter.setIncludeHeaders(true);
            commonsRequestLoggingFilter.setMaxPayloadLength(100000);
        }
        return commonsRequestLoggingFilter;
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>(commonsRequestLoggingFilter());
        registrationBean.addUrlPatterns("/v1/*");
        return registrationBean;
    }

    @Value("${chaeking.flag.common-request-logging-filter}")
    public void setCommonsRequestLoggingFilterFlag(boolean commonsRequestLoggingFilterFlag) {
        LoggingFilterConfig.COMMONS_REQUEST_LOGGING_FILTER_FLAG = commonsRequestLoggingFilterFlag;
    }

}
