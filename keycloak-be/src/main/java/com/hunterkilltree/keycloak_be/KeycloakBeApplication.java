package com.hunterkilltree.keycloak_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KeycloakBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycloakBeApplication.class, args);
    }
}
