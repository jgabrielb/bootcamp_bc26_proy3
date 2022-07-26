package com.nttdata.msaccounts.controller;

import com.nttdata.msaccounts.model.Account;
import com.nttdata.msaccounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService service;

    @GetMapping("/findAll")
    public Flux<Account> getAccounts(){
        return service.findAll();
    }

    @GetMapping("/findAllWithDetail")
    public Flux<Account> getAccountsDetailed(){ return service.findAllWithDetail();}

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Account>> getAccount(@PathVariable String id){
        Mono<Account> newAccount = service.findById(id);
        return newAccount.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/findWithDetailsById/{id}")
    public Mono<ResponseEntity<Account>> getAccountDetailsById(@PathVariable String id){
        Mono<Account> newAccount = service.findByIdWithDetail(id);
        return newAccount.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> createAccount(@RequestBody Account a){
        Mono<Account> newAccount = service.save(a);
        return newAccount;
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Account>> updateAccount(@RequestBody Account a, @PathVariable String id){
        return service.update(a,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable String id){
        return service.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastTenMovements")
    public Flux<Account> getLastTenMovements(){
        return service.lastTenMovements();
    }
}
