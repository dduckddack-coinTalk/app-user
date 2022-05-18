package com.cointalk.user.dto;

import lombok.Data;

@Data
public class ResponseDto {
    String status;
    String message;

    static public ResponseDto error(String message) {
        return new ResponseDto("error", message);
    }

    static public ResponseDto ok(String message) {
        return new ResponseDto("ok", message);
    }

    public ResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
