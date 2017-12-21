package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IndividualClientInformation {
    private Long IndividualClientInformationId;
    private String firstName;
    private String secondName;
    private String lastName;
    private String passportUniqueIdentification;
    private String address;
    private String phoneNumber;
}
