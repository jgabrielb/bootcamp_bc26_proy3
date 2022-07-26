package com.nttdata.mswithdrawals.client;

import com.nttdata.mswithdrawals.model.DebitCard;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class DebitCardClient {
    private WebClient client = WebClient.create("http://ms-debit-card:9013/debitcard");

    public Flux<DebitCard> getAccountDetailByDebitCard(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/accountDetail/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
}
