package com.cointalk.user.service;

import com.cointalk.user.entity.User;
import com.cointalk.user.repository.UserRepository;
import com.cointalk.user.util.Encryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        user.setPassword(Encryption.encrypt(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<Integer> updateUser(User user) {
        String password = Encryption.encrypt(user.getPassword());
        return userRepository.updateUser(password, user.getNickName(), user.getEmail());
    }

    @Override
    public Mono<User> login(User user) {
        String password = Encryption.encrypt(user.getPassword());
        return userRepository.findByEmailAndPassword(user.getEmail(), password);
    }

    @Override
    public Mono<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
