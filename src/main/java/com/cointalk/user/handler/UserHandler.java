package com.cointalk.user.handler;

import com.cointalk.user.config.JwtProvider;
import com.cointalk.user.dto.LoginResponseDto;
import com.cointalk.user.dto.ResponseDto;
import com.cointalk.user.entity.User;
import com.cointalk.user.service.EmailService;
import com.cointalk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RequiredArgsConstructor
@Component
public class UserHandler {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final EmailService sendEmailService;

    public Mono<ServerResponse> test(ServerRequest request) {
        return ok().body(Mono.just("hello user"), String.class);
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return ok().body(userService.getUser(email), User.class);
    }

    public Mono<ServerResponse> getEmailAuthentication(ServerRequest request) {
        String email = request.pathVariable("email");
        var responseDtoMono = userService.getUser(email).map(user -> {
            return new ResponseDto("ok", user.getIsAuthentication().toString());
        })
                .switchIfEmpty(Mono.just(new ResponseDto("error", "등록되지 않은 이메일입니다.")));

        return ok().body(responseDtoMono, ResponseDto.class);
    }

    public Mono<ServerResponse> confirmEmailAuthentication(ServerRequest request) {
        String email = request.pathVariable("email");
        var result = userService.getUser(email)
                .flatMap(user -> (user.getIsAuthentication()) ? Mono.just("이미 인증된 이메일 입니다.")
                        : userService.updateEmailAuthentication(email))
                .switchIfEmpty(Mono.just("등록되지 않은 이메일 입니다."));
        return ok().body(result, String.class);
    }

    public Mono<ServerResponse> emailAuthentication(ServerRequest request) {
        String email = request.pathVariable("email");
        String authUrl = sendEmailService.generateAuthUrl(request, email);
        return userService.emailAuthentication(authUrl, email).flatMap(result -> {
            return ok().body(Mono.just(result), ResponseDto.class);
        });
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<ResponseDto> resultMono = request.bodyToMono(User.class)
                .flatMap(userService::createUser)
                .map(o -> new ResponseDto("ok", "유저 생성 성공"))
                .onErrorReturn(new ResponseDto("error", "유저 생성 실패"));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(resultMono, ResponseDto.class);
    }

    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        String jwt = request.headers().firstHeader("Authorization");

        return request.bodyToMono(User.class)
                .flatMap(user -> userService.updateUser(user).flatMap(o -> {
                    return makeUpdateResponse(user, o, jwt);
                }));
    }

    public Mono<ServerResponse> deleteAccount(ServerRequest request) {
        String email = request.pathVariable("email");
        var result = userService.deleteUser(email).map(o -> o == 0 ? "삭제 안 되었습니다." : "삭제 되었습니다.").onErrorReturn("삭제 " +
                "에러 발생!");
        return ok().body(result, String.class);
    }

    public Mono<ServerResponse> makeUpdateResponse(User user, Integer updateCount, String existedJwt) {
        if (updateCount == 1) {
            return ok().contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", jwtProvider.generateAccessToken(user))
                    .body(Mono.just(new ResponseDto("ok", "유저 변경 성공")), ResponseDto.class);
        } else {
            return badRequest().contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", existedJwt)
                    .body(Mono.just(new ResponseDto("error", "유저 변경 실패")), ResponseDto.class);
        }
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<ResponseDto> loginResultMono = request.bodyToMono(User.class)
                .flatMap(userService::login)
                .map(this::makeLoginResponse)
                .switchIfEmpty(Mono.just(new ResponseDto("error", "유저 로그인 실패")));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(loginResultMono, ResponseDto.class);
    }

    public ResponseDto makeLoginResponse(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new LoginResponseDto("ok", "유저 로그인 성공", accessToken, refreshToken, user);
    }
}
