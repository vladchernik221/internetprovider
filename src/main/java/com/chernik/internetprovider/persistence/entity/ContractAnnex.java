package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ContractAnnex {
    private Long contractAnnexId;
    private String address;
    private Date concludeDate;
    private Boolean canceled;
    private TariffPlan tariffPlan;
    private Contract contract;
}
