package com.example.demo.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration
public class BeanConfiguration {
    @Autowired
    EntityManager entityManager;

    @Bean(name = "restTemplateWithLoadBalance")
    public RestTemplate restTemplateSSL() {
        return new RestTemplate();
    }
}
