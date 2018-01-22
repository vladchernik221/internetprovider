package com.chernik.internetprovider.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class TariffPlan {
    private Long tariffPlanId;
    private String name;
    private String description;
    private Integer downSpeed;
    private Integer upSpeed;
    private Integer includedTraffic;
    private BigDecimal priceOverTraffic;
    private BigDecimal monthlyFee;
    private Boolean archived;
    private List<Discount> discounts;

    public TariffPlan(Long tariffPlanId) {
        this.tariffPlanId = tariffPlanId;
    }
}
