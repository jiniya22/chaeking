package com.chaeking.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.chaeking.api.feignclient")
@Configuration
public class FeignClientConfig {
}
