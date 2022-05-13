package com.cointalk.user.service;

import com.cointalk.user.dto.ResponseDto;
import com.cointalk.user.entity.User;
import com.cointalk.user.repository.UserRepository;
import com.cointalk.user.util.Encryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(Encryption.encrypt(user.getPassword()));
        }
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
    public Mono<ResponseDto> emailAuthentication(String authUrl, String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(userRepository.save(new User(null, email, null, null, null, false)))
                .filter(User::getIsAuthentication)
                .map(user -> new ResponseDto("ok", "이미 인증된 이메일"))
                .switchIfEmpty(Mono.defer(() -> {
                    if (emailService.sendEmailAuthentication(authUrl, email)) {
                        return Mono.just(new ResponseDto("ok", "유저 인증 메일 발송 성공"));
                    } else {
                        return Mono.just(new ResponseDto("error", "유저 인증 메일 발송 실패"));
                    }
                }));
    }

    @Override
    public Mono<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Mono<String> userEmailAuthentication(String email){
        return Mono.just(email + " 인증 완료 되었습니다.");
    }

}
