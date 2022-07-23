package com.nttdata.mswithdrawals.service;

import com.nttdata.mswithdrawals.model.Withdrawals;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WithdrawalsService {
    Flux<Withdrawals> findAll();

    Mono<Withdrawals> save(Withdrawals c);

    Mono<Withdrawals> findById(String id);

    Mono<Withdrawals> update(Withdrawals c, String id);

    Mono<Withdrawals> delete(String id);
}
