package com.nttdata.msdeposits.model;

import lombok.Data;

@Data
public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String docNumber;
    private int typeCustomer;
    private String descTypeCustomer;
}
