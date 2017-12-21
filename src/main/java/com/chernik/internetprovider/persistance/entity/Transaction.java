package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class Transaction {
    private Long transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private Date date;
    private Account account;
}
