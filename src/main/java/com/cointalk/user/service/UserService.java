package com.cointalk.user.service;

import com.cointalk.user.dto.ResponseDto;
import com.cointalk.user.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);

    Mono<User> getUser(String email);

    Mono<Integer> updateUser(User user);

    Mono<Integer> deleteUser(String email);

    Mono<User> login(User user);

    Mono<ResponseDto> emailAuthentication(String authUrl, String email);

    Mono<String> updateEmailAuthentication(String email);

    User changePasswordInUserEntity(User user, String password);

    User changeNickNameInUserEntity(User user, String nickName);
}
