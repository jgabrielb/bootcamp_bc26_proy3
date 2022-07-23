package com.nttdata.mssignatories.controller;

import com.nttdata.mssignatories.model.Signatories;
import com.nttdata.mssignatories.service.SignatoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/signatories")
public class SignatoriesController {
    @Autowired
    private SignatoriesService service;

    @GetMapping("/findAll")
    public Flux<Signatories> getSignatories(){
        return service.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Signatories>> getSignatorie(@PathVariable String id){
        Mono<Signatories> newSignatories = service.findById(id);
        return newSignatories.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Signatories> createSignatories(@RequestBody Signatories c){
        Mono<Signatories> newSignatories = service.save(c);
        return newSignatories;
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Signatories>> updateSignatories(@RequestBody Signatories c, @PathVariable String id){
        return service.update(c,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteSignatories(@PathVariable String id){
        return service.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
