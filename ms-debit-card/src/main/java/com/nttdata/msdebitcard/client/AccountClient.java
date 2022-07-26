package com.nttdata.msdebitcard.client;

import com.nttdata.msdebitcard.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountClient {
    private WebClient client = WebClient.create("http://ms-accounts:9004/accounts");

    public Flux<Account> findAllWithDetail(){
        return client.get()
                .uri("/findAllWithDetail")
                .retrieve()
                .bodyToFlux(Account.class);
    };
}
