package com.cointalk.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        return route().nest(path("/user"), builder -> {
            builder.GET("/test", (req) -> ServerResponse.ok().body(Mono.just("hello user!"), String.class));
        }).build();
    }
}
