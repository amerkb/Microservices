package com.elearning.courseservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced  // هذه الإضافة تسمح للـ WebClient بالتعامل مع أسماء الخدمات بدلاً من العناوين الثابتة
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
