package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ContractAnnex {
    private Long contractAnnexId;
    private String address;
    private Date concludeDate;
    private Boolean dissolved;
    private TariffPlan tariffPlan;
    private Contract contract;
}
