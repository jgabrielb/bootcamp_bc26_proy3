package com.nttdata.msdeposits.controller;

import com.nttdata.msdeposits.model.Deposits;
import com.nttdata.msdeposits.service.DepositsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/deposits")
public class DepositsController {
    @Autowired
    private DepositsService service;

    @GetMapping("/findAll")
    public Flux<Deposits> getDeposits(){
        return service.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Deposits>> getDeposit(@PathVariable String id){
        Mono<Deposits> newDeposits = service.findById(id);
        return newDeposits.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Deposits> createDeposits(@RequestBody Deposits c){
        Mono<Deposits> newDeposits = service.save(c);
        return newDeposits;
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Deposits>> updateDeposits(@RequestBody Deposits c, @PathVariable String id){
        return service.update(c,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteDeposits(@PathVariable String id){
        return service.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
