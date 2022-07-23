package com.nttdata.mspayments.controller;

import com.nttdata.mspayments.model.Payments;
import com.nttdata.mspayments.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payments")
public class PaymentsController {
    @Autowired
    private PaymentsService service;

    @GetMapping("/findAll")
    public Flux<Payments> getPayments(){
        return service.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Payments>> getPayment(@PathVariable String id){
        Mono<Payments> newPayments = service.findById(id);
        return newPayments.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Payments> createPayments(@RequestBody Payments c){
        Mono<Payments> newPayments = service.save(c);
        return newPayments;
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Payments>> updatePayments(@RequestBody Payments c, @PathVariable String id){
        return service.update(c,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deletePayments(@PathVariable String id){
        return service.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
