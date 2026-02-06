package com.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalFallbackController {

    @RequestMapping(value = "/fallback")
    public Mono<Map<String, Object>> fallback(ServerWebExchange exchange) {

        String serviceName = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Service-Name");

        if (serviceName == null) {
            serviceName = "UNKNOWN-SERVICE";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("service", serviceName);
        response.put("message", serviceName + " is currently unavailable. Please try again later.");
        response.put("path", exchange.getRequest().getPath().value());

        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);

        return Mono.just(response);
    }

//    @RequestMapping("/fallback")
//    public String fallback() {
//        return "fallback";
//    }
}

