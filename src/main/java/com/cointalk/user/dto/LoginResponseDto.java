package com.cointalk.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class LoginResponseDto extends ResponseDto{

    private String accessToken;
    private String refreshToken;

    public LoginResponseDto(String status, String message, String accessToken, String refreshToken) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
