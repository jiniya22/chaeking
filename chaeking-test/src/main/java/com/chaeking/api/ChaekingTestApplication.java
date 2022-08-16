package com.chaeking.api;

import com.chaeking.api.config.DatasourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@EnableConfigurationProperties(DatasourceConfig.class)
@SpringBootApplication
public class ChaekingTestApplication {

    public static void main(String ar[]) {
        SpringApplication.run(ChaekingTestApplication.class, ar);
    }

    static String username;

    static String password;

    @Value("${datasource.username}")
    public void setUsername(String username) {
        ChaekingTestApplication.username = username;
    }

    @Value("${datasource.password}")
    public void setPassword(String password) {
        ChaekingTestApplication.password = password;
    }

    @PostConstruct
    private void postConstruct() {
        System.out.println("##########################");
        System.out.println(username);
        System.out.println(password);
        System.out.println("##########################");
    }
}
