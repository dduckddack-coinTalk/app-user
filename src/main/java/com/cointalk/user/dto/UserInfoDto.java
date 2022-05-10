package com.cointalk.user.dto;

import com.cointalk.user.model.UserInfo;
import lombok.Data;


@Data
public class UserInfoDto {
    UserInfo result;
    String status;
    String message;

}
