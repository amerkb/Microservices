package com.elearning.examservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetRole")
    @TimeLimiter(name = "userService")
    public CompletableFuture<String> getRole(String token) {
        return CompletableFuture.supplyAsync(() ->
                webClientBuilder.build()
                        .get()
                        .uri("http://user-service/users/me")
                        .header("Authorization", token)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(map -> (String) map.get("role"))
                        .block()
        );
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserId")
    @TimeLimiter(name = "userService")
    public CompletableFuture<Long> getUserId(String token) {
        return CompletableFuture.supplyAsync(() ->
                webClientBuilder.build()
                        .get()
                        .uri("http://user-service/users/me")
                        .header("Authorization", token)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(map -> ((Number) map.get("id")).longValue())
                        .block()
        );
    }

    // fallback methods
    public CompletableFuture<String> fallbackGetRole(String token, Throwable t) {
        return CompletableFuture.completedFuture("UNKNOWN");
    }

    public CompletableFuture<Long> fallbackGetUserId(String token, Throwable t) {
        return CompletableFuture.completedFuture(-1L);
    }
}
