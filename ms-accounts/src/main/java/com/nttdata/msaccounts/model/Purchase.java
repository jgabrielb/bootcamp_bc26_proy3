package com.nttdata.msaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    private String id;

    private LocalDate purchaseDate;

    private BigDecimal purchaseAmount;
    private String description;
    private String currency;
    private String accountId;
}
