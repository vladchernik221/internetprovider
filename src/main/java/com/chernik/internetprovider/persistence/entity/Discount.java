package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Discount {
    private Long discountId;
    private String name;
    private String description;
    private Integer amount;
    private Date startDate;
    private Date endDate;
    private Boolean onlyForNewClient;
}
