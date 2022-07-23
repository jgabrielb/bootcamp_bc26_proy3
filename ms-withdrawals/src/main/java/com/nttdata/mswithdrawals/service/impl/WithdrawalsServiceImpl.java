package com.nttdata.mswithdrawals.service.impl;

import com.nttdata.mswithdrawals.client.AccountClient;
import com.nttdata.mswithdrawals.model.Withdrawals;
import com.nttdata.mswithdrawals.repository.WithdrawalsRepository;
import com.nttdata.mswithdrawals.service.WithdrawalsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class WithdrawalsServiceImpl implements WithdrawalsService {

    private static Logger logger = LogManager.getLogger(WithdrawalsServiceImpl.class);

    @Autowired
    WithdrawalsRepository repository;

    @Autowired
    AccountClient accountClient;

    @Override
    public Flux<Withdrawals> findAll() {
        logger.info("Executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Withdrawals> save(Withdrawals c) {
        logger.info("Executing save method");
        return accountClient.getAccountWithDetails(c.getAccountId())
                .filter( x -> x.getProduct().getIndProduct() == 1)
                .hasElement()
                .flatMap( y -> {
                    if(y){
                        return repository.save(c);
                    }else{
                        return Mono.error(new RuntimeException("La cuenta ingresada no es una cuenta bancaria"));
                    }
                });
    }

    @Override
    public Mono<Withdrawals> findById(String id) {
        logger.info("Executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Withdrawals> update(Withdrawals c, String id) {
        logger.info("Executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setWithdrawalsDate(c.getWithdrawalsDate());
                    x.setWithdrawalsAmount(c.getWithdrawalsAmount());
                    x.setCurrency(c.getCurrency());
                    x.setAccountId(c.getAccountId());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Withdrawals> delete(String id) {
        logger.info("Executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }
}
