package com.nttdata.msproducts.client;

import com.nttdata.msproducts.model.Deposit;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class DepositClient {
    private WebClient client = WebClient.create("http://ms-deposits:9006/deposits");

    public Flux<Deposit> getDeposits(){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findAll")
                        .build()
                )
                .retrieve()
                .bodyToFlux(Deposit.class);
    };
}
