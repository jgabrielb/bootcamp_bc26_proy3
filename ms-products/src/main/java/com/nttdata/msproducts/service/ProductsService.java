package com.nttdata.msproducts.service;

import com.nttdata.msproducts.model.Products;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductsService {
    Flux<Products> findAll();

    Mono<Products> save(Products c);

    Mono<Products> findById(String id);

    Mono<Products> update(Products c, String id);

    Mono<Products> delete(String id);
}
