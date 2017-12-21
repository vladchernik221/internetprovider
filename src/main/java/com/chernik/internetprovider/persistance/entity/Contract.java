package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Contract {
    private Long contractId;
    private Integer number;
    private Boolean dissolved;
    private ClientType clientType;
    private LegalEntityClientInformation legalEntityClientInformation;
    private IndividualClientInformation individualClientInformation;
    private List<ContractAnnex> contractAnnexes;
}
