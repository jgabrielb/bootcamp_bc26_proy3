package com.nttdata.msproducts.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.msproducts.model.Account;
import com.nttdata.msproducts.model.Products;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ProductsService {
    Flux<Products> findAll();

    Mono<Products> save(Products c);

    Mono<Products> findById(String id);

    Mono<Products> update(Products c, String id);

    Mono<Products> delete(String id);

    Flux<Account> findByDate(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                   LocalDate startDate, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                   LocalDate endDate);
}
