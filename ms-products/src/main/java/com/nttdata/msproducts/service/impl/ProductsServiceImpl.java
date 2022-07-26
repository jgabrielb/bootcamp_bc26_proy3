package com.nttdata.msproducts.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.msproducts.client.*;
import com.nttdata.msproducts.model.*;
import com.nttdata.msproducts.repository.ProductsRepository;
import com.nttdata.msproducts.service.ProductsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

    private static Logger logger = LogManager.getLogger(ProductsServiceImpl.class);

    @Autowired
    ProductsRepository repository;

    @Autowired
    AccountClient accountClient;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    DepositClient depositClient;

    @Autowired
    WithdrawalClient withdrawalClient;

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    PurchaseClient purchaseClient;

    @Autowired
    SignatoriesClient signatoriesClient;

    @Override
    public Flux<Products> findAll() {
        logger.info("executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Products> save(Products c) {
        logger.info("executing save method");
        return repository.save(c);
    }

    @Override
    public Mono<Products> findById(String id) {
        logger.info("executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Products> update(Products c, String id) {
        logger.info("executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setIndProduct(c.getIndProduct());
                    x.setDescIndProduct(c.getDescIndProduct());
                    x.setTypeProduct(c.getTypeProduct());
                    x.setDescTypeProduct(c.getDescTypeProduct());
                    x.setAmountPerDay(c.getAmountPerDay());
                    x.setAmountPerMonth(c.getAmountPerMonth());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Products> delete(String id) {
        logger.info("executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }

    @Override
    public Flux<Account> findByDate(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate startDate, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate endDate) {


        return accountClient.findAll()
                .filter(trans -> (trans.getRegistrationDate().isAfter(startDate) && trans.getRegistrationDate().isBefore(endDate)))
                .flatMap( trans -> customerClient.getCustomer(trans.getCustomerId())
                        .flatMapMany( customer -> {
                            return findById(trans.getProductId())
                                    .flatMapMany( product -> depositClient.getDeposits()
                                            .filter(x -> x.getAccountId().equals(trans.getId()))
                                            .collectList()
                                            .flatMapMany((deposit -> {
                                                return withdrawalClient.getWithdrawal()
                                                        .filter(i -> i.getAccountId().equals(trans.getId()))
                                                        .collectList()
                                                        .flatMapMany(( withdrawals -> {
                                                            return paymentClient.getPayments()
                                                                    .filter(z -> z.getAccountId().equals(trans.getId()))
                                                                    .collectList()
                                                                    .flatMapMany((payments -> {
                                                                        return purchaseClient.getPurchases()
                                                                                .filter(y -> y.getAccountId().equals(trans.getId()))
                                                                                .collectList()
                                                                                .flatMapMany(purchases -> {
                                                                                    return signatoriesClient.getSignatories()
                                                                                            .filter(o -> o.getAccountId().equals(trans.getId()))
                                                                                            .collectList()
                                                                                            .flatMapMany(signatories -> {
                                                                                                ValorAllValidator(trans, customer, product, deposit, withdrawals, payments, purchases, signatories);
                                                                                                return Flux.just(trans);
                                                                                            });
                                                                                });

                                                                    } ));
                                                        } ));
                                            })));
                        }));

    }

    private void ValorAllValidator(Account a, Customer customer, Products product, List<Deposit> deposit, List<Withdrawal> withdrawals, List<Payment> payments, List<Purchase> purchases, List<Signatories> signatories) {
        a.setCustomer(customer);
        a.setProduct(product);
        a.setDeposits(deposit.stream().collect(Collectors.toList()));
        a.setWithdrawals(withdrawals.stream().collect(Collectors.toList()));
        a.setPayments(payments.stream().collect(Collectors.toList()));
        a.setPurchases(purchases.stream().collect(Collectors.toList()));
        a.setSignatories(signatories.stream().collect(Collectors.toList()));
    }
}
