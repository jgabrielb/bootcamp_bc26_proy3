package com.nttdata.msaccounts.client;

import com.nttdata.msaccounts.model.DebitCard;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DebitCardClient {
    private WebClient client = WebClient.create("http://ms-debit-card:9013/debitcard");

    public Flux<DebitCard> getDebitCards(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
}
