package com.nttdata.mscustomers.service.impl;

import com.nttdata.mscustomers.client.*;
import com.nttdata.mscustomers.model.*;
import com.nttdata.mscustomers.repository.CustomerRepository;
import com.nttdata.mscustomers.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

    @Autowired
    CustomerRepository repository;

    @Autowired
    AccountClient accountClient;

    @Autowired
    ProductClient productClient;

    @Autowired
    DepositClient depositClient;

    @Autowired
    WithdrawalClient withDrawalClient;

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    PurchaseClient purchaseClient;

    @Autowired
    SignatoriesClient signatoryClient;

    @Override
    public Flux<Customer> findAll() {
        logger.info("executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Customer> save(Customer c) {
        logger.info("executing save method");
        return repository.save(c);
    }

    @Override
    public Mono<Customer> findById(String id) {
        logger.info("executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Customer> update(Customer c, String id) {
        logger.info("executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                            x.setFirstName(c.getFirstName());
                            x.setLastName(c.getLastName());
                            x.setDocNumber(c.getDocNumber());
                            x.setTypeCustomer(c.getTypeCustomer());
                            x.setDescTypeCustomer(c.getDescTypeCustomer());
                            return repository.save(x);
                });
    }

    @Override
    public Mono<Customer> delete(String id) {
        logger.info("executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }

    @Override
    public Flux<Account> summaryCustomerByProduct() {
        logger.info("executing summaryCustomerByProduct method");
        return accountClient.findAll()
                .flatMap(a -> findById(a.getCustomerId())
                        .flatMapMany(customer -> {
                            return productClient.getProduct(a.getProductId())
                                    .flatMapMany( product -> depositClient.getDeposits()
                                            .filter(x -> x.getAccountId().equals(a.getId()))
                                            .collectList()
                                            .flatMapMany((deposit -> {
                                                return withDrawalClient.getWithdrawal()
                                                        .filter(i -> i.getAccountId().equals(a.getId()))
                                                        .collectList()
                                                        .flatMapMany(( withdrawals -> {
                                                            return paymentClient.getPayments()
                                                                    .filter(z -> z.getAccountId().equals(a.getId()))
                                                                    .collectList()
                                                                    .flatMapMany((payments -> {
                                                                        return purchaseClient.getPurchases()
                                                                                .filter(y -> y.getAccountId().equals(a.getId()))
                                                                                .collectList()
                                                                                .flatMapMany(purchases -> {
                                                                                    return signatoryClient.getSignatories()
                                                                                            .filter(o -> o.getAccountId().equals(a.getId()))
                                                                                            .collectList()
                                                                                            .flatMapMany(signatories -> {
                                                                                                ValorAllValidator(a, customer, product, deposit, withdrawals, payments, purchases, signatories);
                                                                                                return Flux.just(a);
                                                                                            });
                                                                                });

                                                                    } ));
                                                        } ));
                                            })));
                        }));

    }

    private void ValorAllValidator(Account trans, Customer customer, Product product, List<Deposit> deposit, List<Withdrawal> withdrawals, List<Payment> payments, List<Purchase> purchases, List<Signatories> signatories) {
        trans.setCustomer(customer);
        trans.setProduct(product);
        trans.setDeposits(deposit.stream().collect(Collectors.toList()));
        trans.setWithdrawals(withdrawals.stream().collect(Collectors.toList()));
        trans.setPayments(payments.stream().collect(Collectors.toList()));
        trans.setPurchases(purchases.stream().collect(Collectors.toList()));
        trans.setSignatories(signatories.stream().collect(Collectors.toList()));
    }
}
