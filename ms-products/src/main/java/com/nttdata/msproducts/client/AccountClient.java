package com.nttdata.msproducts.client;

import com.nttdata.msproducts.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountClient {
    private WebClient client = WebClient.create("http://ms-accounts:9004/accounts");

    public Mono<Account> getAccountWithDetails(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findWithDetailsById/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Account.class);
    };

    public Mono<Account> updateAccount(Account account){
        return client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/update/{id}")
                        .build(account.getId())
                )
                .bodyValue(account)
                .retrieve()
                .bodyToMono(Account.class);
    };

    public Flux<Account> findAll(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Account.class);
    };

    public Flux<Account> findAllWithDetail(){
        return client.get()
                .uri("/findAllWithDetail")
                .retrieve()
                .bodyToFlux(Account.class);
    };
}
