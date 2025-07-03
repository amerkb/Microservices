package com.elearning.examservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    public String getRole(String token) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/user-service/users/me")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (String) map.get("role"))
                .block();
    }


    public Long getUserId(String token) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/user-service/users/me")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> ((Number) map.get("id")).longValue())
                .block();
    }

}
