package com.nttdata.msaccounts.client;

import com.nttdata.msaccounts.model.Purchase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PurchaseClient {
    private WebClient client = WebClient.create("http://ms-purchase:9009/purchase");

    public Mono<Purchase> getPurchase(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Purchase.class);
    };

    public Flux<Purchase> getPurchases(){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findAll")
                        .build()
                )
                .retrieve()
                .bodyToFlux(Purchase.class);
    };
}
