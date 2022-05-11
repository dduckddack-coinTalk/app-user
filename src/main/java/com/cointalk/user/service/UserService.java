package com.cointalk.user.service;

import com.cointalk.user.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);

    Mono<User> getUser(String email);

    Mono<Integer> updateUser(User user);

    Mono<User> login(User user);

    Mono<String> userEmailAuthentication(String email);
}
