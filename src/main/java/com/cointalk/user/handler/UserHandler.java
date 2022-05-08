package com.cointalk.user.handler;

import com.cointalk.user.entity.User;
import com.cointalk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class UserHandler {
    private final UserService userService;

    public Mono<ServerResponse> test(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("hello user"), String.class);
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return ServerResponse.ok().body(userService.getUser(email), User.class);
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        var result = request.bodyToMono(User.class).flatMap(userService::createUser);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result, User.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }
}
