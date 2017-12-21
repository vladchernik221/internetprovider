package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LegalEntityClientInformation {
    private Long legalEntityClientInformationId;
    private Integer payersAccountNumber;
    private String name;
    private String checkingAccount;
    private String address;
    private String phoneNumber;
}
