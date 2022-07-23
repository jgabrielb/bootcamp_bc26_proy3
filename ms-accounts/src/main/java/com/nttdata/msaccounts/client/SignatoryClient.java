package com.nttdata.msaccounts.client;


import com.nttdata.msaccounts.model.Signatories;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class SignatoryClient {
    private WebClient client = WebClient.create("http://ms-signatories:9005/signatories");

    public Flux<Signatories> getSignatories(){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findAll")
                        .build()
                )
                .retrieve()
                .bodyToFlux(Signatories.class);
    };
}
