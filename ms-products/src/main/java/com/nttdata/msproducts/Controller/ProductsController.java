package com.nttdata.msproducts.Controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.msproducts.model.Account;
import com.nttdata.msproducts.model.Products;
import com.nttdata.msproducts.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsService service;

    @GetMapping("/findAll")
    public Flux<Products> getProducts(){
        return service.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Products>> getProducts(@PathVariable String id){
        Mono<Products> newProducts = service.findById(id);
        return newProducts.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Products> createProducts(@RequestBody Products c){
        Mono<Products> newProducts = service.save(c);
        return newProducts;
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Products>> updateProducts(@RequestBody Products c, @PathVariable String id){
        return service.update(c,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteProducts(@PathVariable String id){
        return service.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByDate/{startDate}/{endDate}")
    public Flux<Account> findByDate(@PathVariable @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate startDate, @PathVariable @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate endDate) {
        return service.findByDate(startDate, endDate);
    }

}
