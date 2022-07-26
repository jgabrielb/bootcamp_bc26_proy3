package com.nttdata.msdeposits.service.impl;

import com.nttdata.msdeposits.client.AccountClient;
import com.nttdata.msdeposits.model.Account;
import com.nttdata.msdeposits.model.Deposits;
import com.nttdata.msdeposits.repository.DepositsRepository;
import com.nttdata.msdeposits.service.DepositsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@Transactional
public class DepositsServiceImpl implements DepositsService{

    private static Logger logger = LogManager.getLogger(DepositsServiceImpl.class);

    @Autowired
    DepositsRepository repository;

    @Autowired
    AccountClient accountClient;

    @Override
    public Flux<Deposits> findAll() {
        logger.info("Executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Deposits> save(Deposits c) {
        logger.info("Executing save method");
        return accountClient.getAccountWithDetails(c.getAccountId())
                .filter( x -> x.getProduct().getIndProduct() == 1)
                .hasElement()
                .flatMap( y -> {
                    if(y){
                        return accountClient.getAccountWithDetails(c.getAccountId()).flatMap(account -> {
                            if(account.getMaxAmountTransaction() > account.getCurrentNumberTransaction()) {
                                return updateCurrentNumberTransaction(accountClient.getAccountWithDetails(c.getAccountId()))
                                        .flatMap(trans -> {
                                            return repository.save(c);
                                        });
                            } else {
                                // Maximo Numero de transacciones, se cobra comision
                                c.setDepositAmount(c.getDepositAmount().add(account.getCommission().multiply(c.getDepositAmount().divide(BigDecimal.valueOf(100)))));
                                return repository.save(c);
                            }
                        });
                    }else{
                        return Mono.error(new RuntimeException("La cuenta ingresada no es una cuenta bancaria"));
                    }
                });
    }

    public Mono<Account> updateCurrentNumberTransaction(Mono<Account> trans) {
        return trans.flatMap(t -> {
            t.setCurrentNumberTransaction(t.getCurrentNumberTransaction()+1);
            return accountClient.updateAccount(t);
        });
    }

    @Override
    public Mono<Deposits> findById(String id) {
        logger.info("Executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Deposits> update(Deposits c, String id) {
        logger.info("Executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setDepositDate(c.getDepositDate());
                    x.setDepositAmount(c.getDepositAmount());
                    x.setCurrency(c.getCurrency());
                    x.setAccountId(c.getAccountId());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Deposits> delete(String id) {
        logger.info("Executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }
}
