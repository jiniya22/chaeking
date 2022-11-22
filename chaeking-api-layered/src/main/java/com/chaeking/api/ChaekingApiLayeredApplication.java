package com.chaeking.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class ChaekingApiLayeredApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.KOREA);
        SpringApplication.run(ChaekingApiLayeredApplication.class, args);
    }

}
