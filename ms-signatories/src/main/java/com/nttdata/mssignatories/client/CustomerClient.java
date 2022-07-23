package com.nttdata.mssignatories.client;

import com.nttdata.mssignatories.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CustomerClient {

    private WebClient client = WebClient.create("http://ms-customers:9002/customers");

    public Mono<Customer> getCustomer(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Customer.class);
    };
}
