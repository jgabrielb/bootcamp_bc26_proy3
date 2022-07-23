package com.nttdata.msdeposits.client;

import com.nttdata.msdeposits.model.Account;
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
}
