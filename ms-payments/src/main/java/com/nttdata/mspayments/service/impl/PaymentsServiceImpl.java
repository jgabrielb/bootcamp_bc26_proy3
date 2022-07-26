package com.nttdata.mspayments.service.impl;

import com.nttdata.mspayments.client.AccountClient;
import com.nttdata.mspayments.model.Payments;
import com.nttdata.mspayments.repository.PaymentsRepository;
import com.nttdata.mspayments.service.PaymentsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PaymentsServiceImpl implements PaymentsService {

    private static Logger logger = LogManager.getLogger(PaymentsServiceImpl.class);

    @Autowired
    PaymentsRepository repository;


    @Autowired
    AccountClient accountClient;

    @Override
    public Flux<Payments> findAll() {
        logger.info("Executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Payments> save(Payments c) {
        logger.info("Executing save method");
        return accountClient.getAccountWithDetails(c.getAccountId())
                .filter( x -> x.getProduct().getIndProduct() == 2)
                .hasElement()
                .flatMap( y -> {
                    if(y){
                        return repository.save(c);
                    }else{
                        return Mono.error(new RuntimeException("La cuenta ingresada no es de tipo credito"));
                    }
                });
    }

    @Override
    public Mono<Payments> findById(String id) {
        logger.info("Executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Payments> update(Payments c, String id) {
        logger.info("Executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setPaymentDate(c.getPaymentDate());
                    x.setPaymentAmount(c.getPaymentAmount());
                    x.setDescription(c.getDescription());
                    x.setCurrency(c.getCurrency());
                    x.setAccountId(c.getAccountId());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Payments> delete(String id) {
        logger.info("Executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }

}
