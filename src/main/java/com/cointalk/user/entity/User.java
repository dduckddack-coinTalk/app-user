package com.cointalk.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("user")
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
}
