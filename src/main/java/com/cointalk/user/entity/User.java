package com.cointalk.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table("user")
@NoArgsConstructor
public class User {
    @Id
    @Column("id")
    private Long id;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("nick_name")
    private String nickName;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("is_authentication")
    private Boolean isAuthentication;

    public User(
            Long id,
            String email,
            String password,
            String nickName,
            LocalDateTime createdAt,
            Boolean isAuthentication) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.createdAt = createdAt;
        this.isAuthentication = isAuthentication;
    }
}
