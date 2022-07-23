package com.nttdata.mscustomers.controller;
import com.nttdata.mscustomers.model.Customer;
import com.nttdata.mscustomers.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/findAll")
    public Flux<Customer> getCustomers(){
        return service.findAll();
    }


    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Customer>> getCustomer(@PathVariable String id){
        Mono<Customer> newCustomer = service.findById(id);
        return newCustomer.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> createCustomer(@RequestBody Customer c){
        Mono<Customer> newCustomer = service.save(c);
        return newCustomer;
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@RequestBody Customer c, @PathVariable String id){
        return service.update(c,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String id){
        return service.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}