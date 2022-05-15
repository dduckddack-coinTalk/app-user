package com.cointalk.user.router;

import com.cointalk.user.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Configuration
public class Router {

    private final UserHandler userHandler;


    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        return route().nest(path("/user"), builder -> {
            builder
                    .GET("/show", userHandler::show)
                    .GET("/test", userHandler::test)
                    .GET("/email/{email}", userHandler::getUserByEmail)
                    .GET("/email/{email}/authentication", userHandler::getEmailAuthentication)
                    .POST("/email/{email}/authentication", userHandler::emailAuthentication)
                    .GET("/email/{email}/authentication/confirm", userHandler::confirmEmailAuthentication)
                    .POST("/account", userHandler::createAccount)
                    .PUT("/account", userHandler::updateAccount)
                    .DELETE("/account/{email}", userHandler::deleteAccount)
                    .POST("/login", userHandler::login)
                    .POST("/upload-dir", userHandler::uploadDir)
            ;
        }).build();
    }


}
