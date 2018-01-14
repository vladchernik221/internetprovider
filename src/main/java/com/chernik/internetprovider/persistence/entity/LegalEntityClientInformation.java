package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LegalEntityClientInformation {
    private Long legalEntityClientInformationId;
    private String payerAccountNumber;
    private String checkingAccount;
    private String name;
    private String address;
    private String phoneNumber;
}
