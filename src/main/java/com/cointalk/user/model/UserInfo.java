package com.cointalk.user.model;

import com.cointalk.user.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class UserInfo {
    private Long id;
    private String email;
    private String nickName;

    public UserInfo(User user) {
        id = user.getId();
        email = user.getEmail();
        nickName = user.getNickName();
    }
}
