package com.nttdata.msdeposits.service;

import com.nttdata.msdeposits.model.Deposits;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface DepositsService {
    Flux<Deposits> findAll();

    Mono<Deposits> save(Deposits c);

    Mono<Deposits> findById(String id);

    Mono<Deposits> update(Deposits c, String id);

    Mono<Deposits> delete(String id);
}
