package com.leaf.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppGatewayAppllication {
    public static void main(String[] args) {
        SpringApplication.run(AppGatewayAppllication.class, args);
    }
}
