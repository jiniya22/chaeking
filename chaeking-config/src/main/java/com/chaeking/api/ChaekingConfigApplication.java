package com.chaeking.api;

import com.chaeking.api.config.DatasourceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@RequiredArgsConstructor
@EnableConfigurationProperties(DatasourceConfig.class)
@SpringBootApplication
public class ChaekingConfigApplication implements CommandLineRunner {
    private final DatasourceConfig datasourceConfig;

    public static void main(String ar[]) {
        SpringApplication.run(DatasourceConfig.class, ar);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.printf("test {} , {}", datasourceConfig.getUserName(), datasourceConfig.getPassword());
    }

}
