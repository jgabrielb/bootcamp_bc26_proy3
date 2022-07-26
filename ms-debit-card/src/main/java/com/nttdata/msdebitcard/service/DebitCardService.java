package com.nttdata.msdebitcard.service;

import com.nttdata.msdebitcard.model.DebitCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {
    Flux<DebitCard> findAll();

    Mono<DebitCard> create(DebitCard dc);

    Mono<DebitCard> findById(String id);

    Mono<DebitCard> update(DebitCard dc, String id);

    Mono<Void> delete(String id);

    Flux<DebitCard> accountDetail(String cardNumber);

    Flux<DebitCard> principalDebitAccount(String cardNumber);
}
