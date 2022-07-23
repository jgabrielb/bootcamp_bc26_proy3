package com.nttdata.mswithdrawals.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Account {
    private String id;

    private String customerId;

    private String productId;

    private String accountNumber;

    private String accountNumberInt;

    private int movementLimits;

    private int movementActually;

    private BigDecimal creditLimits;

    private BigDecimal creditActually;

    private BigDecimal commission;


    private LocalDate movementDate;

    private Product product;
}
