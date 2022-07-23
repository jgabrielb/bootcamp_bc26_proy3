package com.nttdata.mspurchase.service;

import com.nttdata.mspurchase.model.Purchase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PurchaseService {
    Flux<Purchase> findAll();

    Mono<Purchase> save(Purchase c);

    Mono<Purchase> findById(String id);

    Mono<Purchase> update(Purchase c, String id);

    Mono<Purchase> delete(String id);

    Flux<Purchase> findAllByAccountId(String id);
}
