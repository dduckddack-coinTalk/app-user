package com.cointalk.user.service;

import com.cointalk.user.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);
    Mono<User> getUser(String email);
}
