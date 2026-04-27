package com.pagely.marketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MarketserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketserviceApplication.class, args);
    }

}
