package com.nttdata.mswithdrawals.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard {
    @Id
    private String id;
    private String cardNumber;

    @Transient
    private Customer customer;

    @Transient
    private Product product;

    @Transient
    private List<Account> transaction;

    @Transient
    private List<Deposit> deposit;

    @Transient
    private List<Withdrawals> withdrawal;

    @Transient
    private List<Payment> payments;

    @Transient
    private List<Purchase> purchases;

    @Transient
    private List<Signatories> signatories;
}
