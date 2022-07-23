package com.nttdata.mssignatories.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Transient;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate movementDate;

    private Customer customer;

    private Product product;

}
