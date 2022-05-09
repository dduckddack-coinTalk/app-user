package com.cointalk.user.model;

import io.jsonwebtoken.Claims;
import lombok.Data;

@Data
public class TokenInfo {
    private long id;
    private String email;
    private String nickName;

    public TokenInfo(long id, String email, String nickName) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
    }

    public static TokenInfo create(Claims tokenClaims) {
        long id =  Long.parseLong(tokenClaims.get("id").toString());
        String email= tokenClaims.get("email").toString();
        String nickName =  tokenClaims.get("nickName").toString();

        return new TokenInfo(id, email, nickName);
    }
}
