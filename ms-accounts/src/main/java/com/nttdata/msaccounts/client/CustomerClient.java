package com.nttdata.msaccounts.client;

import com.nttdata.msaccounts.model.Customer;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerClient {
    private WebClient client = WebClient.create("http://ms-customers:9002/customers");
    private final ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    public Mono<Customer> getCustomer(String id){
      return client.get()
              .uri(uriBuilder -> uriBuilder
                      .path("/find/{id}")
                      .build(id)
              )
              .retrieve()
              .bodyToMono(Customer.class)
              .transform( it -> {
                  ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("ms-customers-client");

                  Customer nCustomer = new Customer();
                  nCustomer.setId(ObjectId.get().toString());
                  nCustomer.setFirstName("EmpleadoPrueba");
                  nCustomer.setLastName("prueba");
                  nCustomer.setDocNumber("63748512");
                  nCustomer.setTypeCustomer(1);
                  nCustomer.setDescTypeCustomer("PERSONAL");

                  return rcb.run(it, throwable -> Mono.just(nCustomer));
              });
    };
}
