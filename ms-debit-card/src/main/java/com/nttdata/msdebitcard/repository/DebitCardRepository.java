package com.nttdata.msdebitcard.repository;

import com.nttdata.msdebitcard.model.DebitCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DebitCardRepository  extends ReactiveCrudRepository<DebitCard, String> {
    Mono<DebitCard> findByTransactionId(String id);

    Mono<DebitCard> findByCardNumber(String cardNumber);
}
