package com.chernik.internetprovider.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Discount {
    private Long discountId;
    private String description;
    private Integer amount;
    private Date startDate;
    private Date endDate;
    private Boolean onlyForNewClient;
}
