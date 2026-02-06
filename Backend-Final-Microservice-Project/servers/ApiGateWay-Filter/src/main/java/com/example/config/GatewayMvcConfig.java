package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@Configuration
public class GatewayMvcConfig {

    @Bean
    public RouteLocator myGatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()

                // ================= USER SERVICE =================
                .route(r -> r.path("/api/users/**")
                        .filters(f -> f
                                .rewritePath("/api/?(?<remaining>.*)", "/api/${remaining}")
                                .addRequestHeader("X-Service-Name", "USER-SERVICES")

                                // 1️⃣ RETRY FIRST
                                .retry(retry -> retry
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(
                                                Duration.ofMillis(100),
                                                Duration.ofMillis(800),
                                                2,
                                                true
                                        )
                                )

                                // 2️⃣ CIRCUIT BREAKER AFTER RETRY
                                .circuitBreaker(cb -> cb
                                        .setName("userServiceCB")
                                        .setFallbackUri("forward:/fallback")
                                )
                        )
                        .uri("lb://USER-SERVICES")
                )

                // ================= PROFILE SERVICE =================
                .route(r -> r.path("/api/profile/**")
                        .filters(f -> f
                                .rewritePath("/api/profile/?(?<remaining>.*)", "/api/profile/${remaining}")
                                .addRequestHeader("X-Service-Name", "PROFILE-SERVICE")

                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setMethods(HttpMethod.GET)
                                )

                                .circuitBreaker(cb -> cb
                                        .setName("profileServiceCB")
                                        .setFallbackUri("forward:/fallback")
                                )
                        )
                        .uri("lb://PROFILE-SERVICE")
                )

                // ================= LOGIN + REGISTER SERVICE =================
                .route(r -> r.path("/api/login/**", "/api/register/**")
                        .filters(f -> f
//                                .rewritePath("/api/(login|register)/?(?<remaining>.*)", "/api/${remaining}")
                                .addRequestHeader("X-Service-Name", "LOGIN-SERVICE")

                                .retry(retry -> retry
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                )

                                .circuitBreaker(cb -> cb
                                        .setName("loginServiceCB")
                                        .setFallbackUri("forward:/fallback")
                                )
                        )
                        .uri("lb://LOGIN-SERVICE")
                )

                // ================= AUDIT SERVICE =================
                .route(r -> r.path("/api/audit/**")
                        .filters(f -> f
                                .rewritePath("/api/audit/?(?<remaining>.*)", "/api/audit/${remaining}")
                                .addRequestHeader("X-Service-Name", "AUDIT-SERVICE")

                                .retry(retry -> retry
                                        .setRetries(1)
                                        .setMethods(HttpMethod.GET)
                                )

                                .circuitBreaker(cb -> cb
                                        .setName("auditServiceCB")
                                        .setFallbackUri("forward:/fallback")
                                )
                        )
                        .uri("lb://AUDIT-SERVICE")
                )

                .build();
    }

    private CorsConfigurationSource corsConfigSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}

