package com.nttdata.msaccounts.client;

import com.nttdata.msaccounts.model.Withdrawal;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WithdrawalClient {
    private WebClient client = WebClient.create("http://ms-withdrawals:9007/withdrawals");

    public Mono<Withdrawal> getWithdrawal(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Withdrawal.class);
    };

    public Flux<Withdrawal> getWithdrawals(){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findAll")
                        .build()
                )
                .retrieve()
                .bodyToFlux(Withdrawal.class);
    };
}
