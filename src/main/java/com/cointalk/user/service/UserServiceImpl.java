package com.cointalk.user.service;

import com.cointalk.user.dto.ResponseDto;
import com.cointalk.user.entity.User;
import com.cointalk.user.repository.UserRepository;
import com.cointalk.user.util.Encryption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AwsUploadService awsUploadService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    @Value("${cloud.aws.s3.bucket.my.download.url}")
    private String bucketUrl;

    public Mono<User> createUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(Encryption.encrypt(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public Mono<Integer> updateUser(User user) {
        return userRepository.updateUser(user.getPassword(), user.getNickName(), user.getImagePath(), user.getEmail());
    }

    public Mono<Integer> deleteUser(String email) {
        return userRepository.deleteByEmail(email);
    }

    @Override
    public Mono<User> login(User user) {
        String password = Encryption.encrypt(user.getPassword());
        return userRepository.findByEmailAndPassword(user.getEmail(), password);
    }

    @Override
    public Mono<ResponseDto> emailAuthentication(String authUrl, String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(userRepository.save(new User(null, email, null, null, null, false, null)))
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
    public Mono<String> updateEmailAuthentication(String email) {
        return userRepository.updateEmailAuthentication(email)
                .flatMap(result -> Mono.just("인증 완료 되었습니다"))
                .onErrorReturn("인증이 실패했습니다.");
    }

    @Override
    public User changePasswordInUserEntity(User user, String password) {
        var encryptPassword = Encryption.encrypt(password);
        if (!encryptPassword.equals(user.getPassword())) {
            user.setPassword(encryptPassword);
        }
        return user;
    }

    public Mono<User> changeImagePathInUserEntity(User user, Part partData) {
        if (partData == null) {
            return Mono.empty();
        }
        String fileName = ((FilePart) partData).filename();
        return awsUploadService.uploadImage(partData)
                .map(isSuccess -> {
                    if (isSuccess) {user.setImagePath(bucketUrl + fileName);}
                    return user;
                });
    }
}
