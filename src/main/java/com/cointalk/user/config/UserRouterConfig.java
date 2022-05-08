package com.cointalk.user.config;

import com.cointalk.user.entity.User;
import com.cointalk.user.repository.UserRepository;
import com.cointalk.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterConfig {

    private final UserRepository userRepository;
    private final UserService userService;


    public UserRouterConfig(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        return route().nest(path("/user"), builder -> {
            builder
                    .GET("/test", (req) -> ServerResponse.ok().body(Mono.just("hello user!"), String.class))
                    .GET("/email/{email}", (req) -> {
                        String email = req.pathVariable("email");
                        return ServerResponse.ok().body(userRepository.findByEmail(email), User.class);
                    })
                    .POST("/account", (request -> {
                        var result = request.bodyToMono(User.class).flatMap(userService::createUser);
                        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result, User.class)
                                .onErrorResume(error -> ServerResponse.badRequest().build());
                    }))
            ;

        }).build();
    }
}
