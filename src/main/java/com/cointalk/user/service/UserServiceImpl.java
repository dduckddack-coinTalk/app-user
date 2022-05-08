package com.cointalk.user.service;

import com.cointalk.user.entity.User;
import com.cointalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<Integer> updateUser(User user) {
        return userRepository.updateUser(user.getPassword(), user.getNickName(), user.getEmail());
    }

    @Override
    public Mono<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
