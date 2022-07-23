package com.nttdata.msaccounts.client;

import com.nttdata.msaccounts.model.Payment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PaymentClient {
    private WebClient client = WebClient.create("http://ms-payments:9008/payments");

    public Mono<Payment> getPayment(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Payment.class);
    };

    public Flux<Payment> getPayments(){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findAll")
                        .build()
                )
                .retrieve()
                .bodyToFlux(Payment.class);
    };
}
