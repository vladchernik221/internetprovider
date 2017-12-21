package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class Account {
    private Long accountId;
    private BigDecimal balance;
    private Integer traffickedTraffic;
    private ContractAnnex contractAnnex;
    private List<Transaction> transactions;
}
