package com.example.shopbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing

public class ShopBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopBatchApplication.class, args);
    }

}
