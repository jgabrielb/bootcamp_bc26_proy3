package com.nttdata.msdebitcard.controller;

import com.nttdata.msdebitcard.model.DebitCard;
import com.nttdata.msdebitcard.service.DebitCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debitcard")
public class DebitCardController {
    private static final Logger LOG = LogManager
            .getLogger(DebitCardController.class);

    @Autowired
    DebitCardService service;

    @GetMapping("/findAll")
    public Flux<DebitCard> getDebitCards() {
        LOG.info("Service call FindAll - DebitCard");
        return service.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<DebitCard> getDebitCard(@PathVariable String id) {
        LOG.info("Service call FindById - DebitCard");
        return service.findById(id);
    }

    @GetMapping("/accountDetail/{id}")
    public Flux<DebitCard> accountDetail(@PathVariable String id) {
        LOG.info("Service call accountDetail - DebitCard");
        return service.accountDetail(id);
    }

    @GetMapping("/principalDebitAccount/{id}")
    public Flux<DebitCard> principalDebitAccount(@PathVariable String id) {
        LOG.info("Service call principalDebitAccount - DebitCard");
        return service.principalDebitAccount(id);
    }

    @PostMapping("/create")
    public Mono<DebitCard> createDebitCard(@RequestBody DebitCard p) {
        LOG.info("Service call create - DebitCard");
        return service.create(p);
    }

    @PutMapping("/update/{id}")
    public Mono<DebitCard> updateDebitCard(@RequestBody DebitCard p, @PathVariable String id) {
        LOG.info("Service call Update - DebitCard");
        return service.update(p, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteDebitCard(@PathVariable String id) {
        LOG.info("Service call delete - DebitCard");
        return service.delete(id);
    }
}
