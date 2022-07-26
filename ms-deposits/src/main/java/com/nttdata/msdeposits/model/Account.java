package com.nttdata.msdeposits.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

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

    private String cardNumber;

    private int maxAmountTransaction;

    private int currentNumberTransaction;

    private LocalDate movementDate;

    private Customer customer;

    private Product product;
}
