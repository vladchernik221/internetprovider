package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

//TODO dissolved date
@Data
@NoArgsConstructor
public class Contract {
    private Long contractId;
    private Date concludeDate;
    private Boolean dissolved;
    private ClientType clientType;
    private LegalEntityClientInformation legalEntityClientInformation;
    private IndividualClientInformation individualClientInformation;
    private List<ContractAnnex> contractAnnexes;
}
