package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IndividualClientInformation {
    private Long individualClientInformationId;
    private String firstName;
    private String secondName;
    private String lastName;
    private String passportUniqueIdentification;
    private String address;
    private String phoneNumber;
}
