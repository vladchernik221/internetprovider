package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long userId;
    private String login;
    private String password;
    private UserRole userRole;
    private Boolean blocked;
    private Contract contract;

    public User(Long userId) {
        this.userId = userId;
    }
}
