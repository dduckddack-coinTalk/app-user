package com.cointalk.user.dto;

import com.cointalk.user.entity.User;
import com.cointalk.user.model.UserInfo;
import lombok.*;

@Getter
@Setter
@ToString
public class LoginResponseDto extends ResponseDto{

    private String accessToken;
    private String refreshToken;
    private UserInfo userInfo;

    public LoginResponseDto(String status, String message, String accessToken, String refreshToken, User user) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userInfo = new UserInfo(user);
    }
}
