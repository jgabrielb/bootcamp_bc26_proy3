package com.nttdata.mscustomers.service;

import com.nttdata.mscustomers.model.Customer;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> findAll();

    Mono<Customer> save(Customer c);

    Mono<Customer> findById(String id);

    Mono<Customer> update(Customer c, String id);

    Mono<Customer> delete(String id);
}
