package com.be88be.wclient.example.controller;

import com.be88be.wclient.example.model.Tooo;
import com.be88be.wclient.example.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.beans.ConstructorProperties;


@RequiredArgsConstructor(
        onConstructor_ = @ConstructorProperties({"webClientService"}))
@Slf4j
@RestController
public class WebClientController {
    private final WebClientService webClientService;

    // 가장 기본적인 webclient 사용 예제.
    // 1개의 값을 리턴할 때는 bodyToMono, 복수의 값을 리턴할 때는 bodyToFlux를 사용한다.
    @GetMapping("/test")
    public Mono<String> doTest() {
        WebClient client = WebClient.create();
        return client.get()
                .uri("http://localhost:9900/webclient/test-create")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/test2")
    public Mono<Tooo> doTest2() {
        return webClientService.getToooByIdNonblock("11");
    }

}
