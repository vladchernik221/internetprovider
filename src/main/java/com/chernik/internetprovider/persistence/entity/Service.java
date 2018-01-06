package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Service {
    private Long serviceId;
    private String name;
    private String description;
    private Boolean archived;
    private BigDecimal price;
}
