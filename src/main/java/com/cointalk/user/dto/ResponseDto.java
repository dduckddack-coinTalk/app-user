package com.cointalk.user.dto;

import lombok.Data;

@Data
public class ResponseDto {
    String status;
    String message;

    public ResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
