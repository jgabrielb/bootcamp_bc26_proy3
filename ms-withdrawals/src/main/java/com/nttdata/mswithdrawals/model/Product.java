package com.nttdata.mswithdrawals.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private String id;
    private int indProduct;
    private String descIndProduct;
    private int typeProduct;
    private String descTypeProduct;
    private BigDecimal amountPerMonth;
    private BigDecimal amountPerDay;
}
