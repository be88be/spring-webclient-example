package com.be88be.wclient.example.service;

import com.be88be.wclient.example.model.Tooo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientService {
    private static final String USERS_URL_TEMPLATE = "/users/{id}";
    private static final String BROKEN_URL_TEMPLATE = "/broken-url/{id}";
    public static final int DELAY_MILLIS = 100;
    public static final int MAX_RETRY_ATTEMPTS = 3;

    private final WebClient webClient;

    public Mono<Tooo> getToooByIdNonblock(String id) {
        return webClient
                .get()
                .uri(USERS_URL_TEMPLATE, id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError()
                        , clientResponse -> clientResponse.bodyToMono(String.class).map(body -> new Exception(body)))
                .bodyToMono(Tooo.class)
                .log();

    }

    //mutate 설정
    public Mono<Tooo> getToooByIdMNonblock(String id) {
        return webClient
                .mutate()
                .baseUrl("https://api.sample.com")
                .build()
                .get()
                .uri(USERS_URL_TEMPLATE, id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError()
                        , clientResponse -> clientResponse.bodyToMono(String.class).map(body -> new Exception(body)))
                .bodyToMono(Tooo.class)
                .log();

    }

    //retry 설정
    public Tooo getToooWithRetryBlock(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(Tooo.class)
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    //Fallback 설정
    public Tooo getToooWithFallbackBlock(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(Tooo.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new Tooo()))
                .block();
    }

    //Error 핸들링 설정
    public Tooo getToooWithErrorHandlingBlock(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Tooo.class)
                .block();
    }
}
