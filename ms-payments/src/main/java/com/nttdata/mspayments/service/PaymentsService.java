package com.nttdata.mspayments.service;

import com.nttdata.mspayments.model.Payments;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentsService {
    Flux<Payments> findAll();

    Mono<Payments> save(Payments c);

    Mono<Payments> findById(String id);

    Mono<Payments> update(Payments c, String id);

    Mono<Payments> delete(String id);
}
