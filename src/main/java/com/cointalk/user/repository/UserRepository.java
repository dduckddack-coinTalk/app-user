package com.cointalk.user.repository;

import com.cointalk.user.entity.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByEmail(String email);

    Mono<User> findByEmailAndPassword(String email, String password);

    Mono<Integer> deleteByEmail(String email);

    @Modifying
    @Query("UPDATE user SET password = :password, nick_name = :nickName , image_path = :imagePath WHERE email = :email")
    Mono<Integer> updateUser(String password, String nickName, String imagePath ,String email);

    @Modifying
    @Query("UPDATE user SET is_authentication = TRUE WHERE email = :email")
    Mono<Integer> updateEmailAuthentication(String email);

}
