package com.nttdata.mswithdrawals.service.impl;

import com.nttdata.mswithdrawals.client.AccountClient;
import com.nttdata.mswithdrawals.client.DebitCardClient;
import com.nttdata.mswithdrawals.model.Account;
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

import java.math.BigDecimal;

@Service
@Transactional
public class WithdrawalsServiceImpl implements WithdrawalsService {

    private static Logger logger = LogManager.getLogger(WithdrawalsServiceImpl.class);

    @Autowired
    WithdrawalsRepository repository;

    @Autowired
    AccountClient accountClient;

    @Autowired
    DebitCardClient debitCardClient;

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
                        return accountClient.getAccountWithDetails(c.getAccountId()).flatMap(account -> {
                            if(account.getMaxAmountTransaction() > account.getCurrentNumberTransaction()) {
                                return updateCurrentNumberTransaction(accountClient.getAccountWithDetails(c.getAccountId()))
                                        .flatMap(trans -> {
                                            return validateDebitCard(c);
                                        });
                            } else {
                                // Maximo Numero de transacciones, se cobra comision
                                c.setWithdrawalsAmount(c.getWithdrawalsAmount().add(account.getCommission().multiply(c.getWithdrawalsAmount().divide(BigDecimal.valueOf(100)))));
                                return validateDebitCard(c);
                            }
                        });
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

    public Mono<Account> updateCurrentNumberTransaction(Mono<Account> trans) {
        return trans.flatMap(t -> {
            t.setCurrentNumberTransaction(t.getCurrentNumberTransaction()+1);
            return accountClient.updateTransaction(t);
        });
    }

    private Mono<Withdrawals> validateDebitCard(Withdrawals withdrawal){
        return accountClient.getAccountWithDetails(withdrawal.getAccountId())
                .flatMap(trans -> debitCardClient.getAccountDetailByDebitCard(trans.getCardNumber())
                        .collectList()
                        .flatMap(dc -> {
                            Account otrans = dc.stream().findFirst().get().getTransaction().stream().filter(t -> t.getProduct().getIndProduct() == 2 && t.getCreditActually().compareTo(withdrawal.getWithdrawalsAmount()) >= 0).findFirst().get();
                            if (otrans != null) {
                                withdrawal.setAccountId(otrans.getId());
                                return repository.save(withdrawal);
                            }else {
                                return Mono.error(new RuntimeException("No hay cuentas con sado disponible"));
                            }
                        }));
    }
}
