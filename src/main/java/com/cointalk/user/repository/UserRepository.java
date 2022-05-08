package com.cointalk.user.repository;

import com.cointalk.user.entity.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    @Query("SELECT * FROM user where email = :email")
    Mono<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE user SET password = :password , nick_name = :nickName WHERE email = :email")
    Mono<Integer> updateUser(String password, String nickName, String email);
}
