package com.nttdata.msaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard {
    private String id;
    private String cardNumber;

    @Transient
    private Customer customer;

    @Transient
    private Product product;

    @Transient
    private List<Deposit> deposit;

    @Transient
    private List<Withdrawal> withdrawal;

    @Transient
    private List<Payment> payments;

    @Transient
    private List<Purchase> purchases;

    @Transient
    private List<Signatories> signatories;
}
