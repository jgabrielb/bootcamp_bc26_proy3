package com.nttdata.msproducts.client;

import com.nttdata.msproducts.model.Signatories;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class SignatoriesClient {
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
