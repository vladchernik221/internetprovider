package com.chernik.internetprovider.persistence.entity;

import com.chernik.internetprovider.persistence.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Account {
    private BigDecimal balance;
    private Integer usedTraffic;
    private ContractAnnex contractAnnex;
    private Page<Transaction> transactions;
}
