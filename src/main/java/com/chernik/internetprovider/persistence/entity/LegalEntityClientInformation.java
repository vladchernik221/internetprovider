package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LegalEntityClientInformation {
    private Long legalEntityClientInformationId;
    private Integer payerAccountNumber;
    private String name;
    private String address;
    private String phoneNumber;
}
