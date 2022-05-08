package com.cointalk.user.handler;

import com.cointalk.user.dto.ResponseDto;
import com.cointalk.user.entity.User;
import com.cointalk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RequiredArgsConstructor
@Component
public class UserHandler {
    private final UserService userService;

    public Mono<ServerResponse> test(ServerRequest request) {
        return ok().body(Mono.just("hello user"), String.class);
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return ok().body(userService.getUser(email), User.class);
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<ResponseDto> resultMono = request.bodyToMono(User.class)
                .flatMap(userService::createUser)
                .map(o -> new ResponseDto("ok", "유저 생성 성공"))
                .onErrorReturn(new ResponseDto("error", "유저 생성 실패"));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(resultMono, ResponseDto.class);
    }

    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        Mono<ResponseDto> resultMono = request.bodyToMono(User.class)
                .flatMap(userService::updateUser)
                .map(o -> {
                    if (o == 0) {
                        return new ResponseDto("error", "유저 변경 실패");
                    } else {
                        return new ResponseDto("ok", "유저 변경 성공");
                    }
                })
                .onErrorReturn(new ResponseDto("error", "유저 변경 실패"));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(resultMono, ResponseDto.class);
    }
}
