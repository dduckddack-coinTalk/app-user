package com.cointalk.user.handler;

import com.cointalk.user.config.JwtProvider;
import com.cointalk.user.dto.LoginResponseDto;
import com.cointalk.user.dto.ResponseDto;
import com.cointalk.user.entity.User;
import com.cointalk.user.service.AwsUploadService;
import com.cointalk.user.service.EmailService;
import com.cointalk.user.service.UserService;
import com.cointalk.user.util.PartParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@RequiredArgsConstructor
@Component
public class UserHandler {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final EmailService sendEmailService;
    private final AwsUploadService awsUploadService;

    public Mono<ServerResponse> show(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_HTML).render("index");
    }

    public Mono<ServerResponse> test(ServerRequest request) {
        return ok().body(Mono.just("hello user"), String.class);
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return ok().body(userService.getUser(email), User.class);
    }

    public Mono<ServerResponse> getEmailAuthentication(ServerRequest request) {
        String email = request.pathVariable("email");
        Mono<ResponseDto> iAuthenticationResponse = userService.getUser(email)
                .map(user -> user.getIsAuthentication().toString())
                .map(ResponseDto::ok)
                .defaultIfEmpty(ResponseDto.error("???????????? ?????? ??????????????????."));

        return ok().body(iAuthenticationResponse, ResponseDto.class);
    }

    public Mono<ServerResponse> confirmEmailAuthentication(ServerRequest request) {
        String email = request.pathVariable("email");
        Mono<String> emailResponse = userService.getUser(email)
                .flatMap(user -> (user.getIsAuthentication())
                        ? Mono.just("?????? ????????? ????????? ?????????.")
                        : userService.updateEmailAuthentication(email))
                .switchIfEmpty(Mono.just("???????????? ?????? ????????? ?????????."));

        return ok().body(emailResponse, String.class);
    }

    public Mono<ServerResponse> emailAuthentication(ServerRequest request) {
        String email = request.pathVariable("email");
        String authUrl = sendEmailService.generateAuthUrl(request, email);
        Mono<ResponseDto> result = userService.emailAuthentication(authUrl, email);
        return ok().body(result, ResponseDto.class);
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<ResponseDto> resultMono = request.bodyToMono(User.class)
                .flatMap(userService::createUser)
                .thenReturn(ResponseDto.ok("?????? ?????? ??????"))
                .onErrorReturn(ResponseDto.error("?????? ?????? ??????"));

        return ok().body(resultMono, ResponseDto.class);
    }

    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        String jwt = request.headers().firstHeader("Authorization");
        Mono<MultiValueMap<String, Part>> multipartDataMono = request.multipartData();
        return multipartDataMono.flatMap(multipartData -> {
            Part email = multipartData.getFirst("email");
            return PartParser.convertString(email)
                    .flatMap(userService::getUser)
                    .doOnNext(user -> {
                        var fileData = multipartData.getFirst("file");
                        userService.changeImagePathInUserEntity(user, fileData).subscribe();
                    })
                    .doOnNext(user -> PartParser
                            .getStringFrom(multipartData, "password")
                            .map(password -> userService.changePasswordInUserEntity(user, password))
                            .subscribe()
                    )
                    .doOnNext(user -> PartParser
                            .getStringFrom(multipartData, "nickName")
                            .doOnNext(user::setNickName)
                            .subscribe()
                    )
                    .flatMap(user -> userService
                            .updateUser(user)
                            .flatMap(updateCount -> makeUpdateResponse(user, updateCount, jwt))
                    )
                    .switchIfEmpty(badRequest().body(Mono.just(ResponseDto.error("???????????? ?????? ??????????????????.")), ResponseDto.class));
        });
    }

    public Mono<ServerResponse> deleteAccount(ServerRequest request) {
        String email = request.pathVariable("email");
        Mono<String> result = userService
                .deleteUser(email)
                .map(o -> o == 0 ? "?????? ?????? ???????????????." : "?????? ???????????????.")
                .onErrorReturn("?????? ?????? ??????!");

        return ok().body(result, String.class);
    }

    public Mono<ServerResponse> makeUpdateResponse(User user, Integer updateCount, String existedJwt) {
        if (updateCount == 1) {
            return ok()
                    .header("Authorization", jwtProvider.generateAccessToken(user))
                    .body(Mono.just(ResponseDto.ok("?????? ?????? ??????")), ResponseDto.class);
        } else {
            return badRequest()
                    .header("Authorization", existedJwt)
                    .body(Mono.just(ResponseDto.error("?????? ?????? ??????")), ResponseDto.class);
        }
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<ResponseDto> loginResultMono = request.bodyToMono(User.class)
                .flatMap(userService::login)
                .map(this::makeLoginResponse)
                .defaultIfEmpty(ResponseDto.error("?????? ????????? ??????"));

        return ok().body(loginResultMono, ResponseDto.class);
    }

    public ResponseDto makeLoginResponse(User user) {
        return new LoginResponseDto("ok", "?????? ????????? ??????",
                jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user), user);
    }

    public void saveFile(ServerRequest request) {
        request.multipartData().map(o -> {
            Part paringFile = o.get("file").get(0);
            FilePart oneFile = (FilePart) paringFile;
            oneFile.transferTo(Paths.get("./src/main/resources/images/" + oneFile.filename())).subscribe();
            return null;
        });
    }

    public Mono<ServerResponse> uploadDir(ServerRequest request) {
        Mono<Boolean> a = request.multipartData().flatMap(o -> {
            Part parsingData = o.get("file").get(0);
            if (parsingData == null) {
                return Mono.just(false);
            }
            return awsUploadService.uploadImage(parsingData);
        });
        return ok().body(a, Boolean.class);
    }
}
