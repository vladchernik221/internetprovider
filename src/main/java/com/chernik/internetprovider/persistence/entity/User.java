package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long UserId;
    private String login;
    private String password;
    private UserRole userRole;
    private Boolean blocked;
    private ContractAnnex contractAnnex;
}
