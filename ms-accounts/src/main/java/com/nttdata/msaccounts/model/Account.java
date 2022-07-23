package com.nttdata.msaccounts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "schema_suscribe.accounts")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
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

    @Transient
    private Customer customer;

    @Transient
    private Product product;

    @Transient
    private List<Deposit> deposits;

    @Transient
    private List<Withdrawal> withdrawals;

    @Transient
    private List<Payment> payments;

    @Transient
    private List<Purchase> purchases;

    @Transient
    private List<Signatories> signatories;

}
