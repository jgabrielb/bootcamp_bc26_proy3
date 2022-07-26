package com.nttdata.mscustomers.client;

import com.nttdata.mscustomers.model.Payment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class PaymentClient {
    private WebClient client = WebClient.create("http://ms-payments:9008/payments");

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
