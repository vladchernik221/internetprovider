package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TariffPlan {
    private Long tariffPlanId;
    private String name;
    private String description;
    private Integer downSpeed;
    private Integer upSpeed;
    private Integer includedTraffic;
    private Integer priceOverTraffic;
    private BigDecimal monthlyFee;
    private Boolean archived;
}
