package com.nttdata.msproducts.client;

import com.nttdata.msproducts.model.Withdrawal;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class WithdrawalClient {
    private WebClient client = WebClient.create("http://ms-withdrawals:9007/withdrawals");

    public Flux<Withdrawal> getWithdrawal(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Withdrawal.class);
    }
}
