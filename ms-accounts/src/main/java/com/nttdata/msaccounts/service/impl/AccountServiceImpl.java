package com.nttdata.msaccounts.service.impl;

import com.nttdata.msaccounts.client.*;
import com.nttdata.msaccounts.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nttdata.msaccounts.repository.AccountRepository;
import com.nttdata.msaccounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    AccountRepository repository;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ProductClient productClient;

    @Autowired
    DepositClient depositClient;

    @Autowired
    WithdrawalClient withdrawalClient;

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    PurchaseClient purchaseClient;

    @Autowired
    SignatoryClient signatoryClient;

    @Override
    public Flux<Account> findAll() {
        logger.info("Executing findAll method");
        return repository.findAll();
    }

    @Override
    public Flux<Account> findAllWithDetail() {
        logger.info("Executing findAllWithDetail method");
        return repository.findAll()
                .flatMap( x -> {
                   return customerClient.getCustomer(x.getCustomerId())
                           .flatMapMany( y -> {
                               return productClient.getProduct(x.getProductId())
                                       .flatMapMany( z -> {
                                           return depositClient.getDeposits()
                                                   .filter( val1 -> val1.getAccountId().equals(x.getId()))
                                                   .collectList()
                                                   .flatMapMany( w -> {
                                                       return withdrawalClient.getWithdrawals()
                                                               .filter( val2 -> val2.getAccountId().equals(x.getId()))
                                                               .collectList()
                                                               .flatMapMany( v -> {
                                                                   return purchaseClient.getPurchases()
                                                                           .filter( val3 -> val3.getAccountId().equals(x.getId()))
                                                                           .collectList()
                                                                           .flatMapMany( t -> {
                                                                              return paymentClient.getPayments()
                                                                                      .filter( val4 -> val4.getAccountId().equals(x.getId()))
                                                                                      .collectList()
                                                                                      .flatMapMany( o -> {
                                                                                          return signatoryClient.getSignatories()
                                                                                                  .filter( val5 -> val5.getAccountId().equals(x.getId()))
                                                                                                  .collectList()
                                                                                                  .flatMapMany( p -> {
                                                                                                      this.setAccountValues(x, y, z, w, v, t, o, p);
                                                                                                      return Flux.just(x);
                                                                                                  });
                                                                                      });
                                                                           });
                                                               });
                                                   });
                                       });
                           });
                });
    }

    @Override
    public Mono<Account> save(Account a) {
        logger.info("Executing save method");

        return this.findAllWithDetail()
                .filter( x -> x.getCustomerId().equals(a.getCustomerId())) // Buscamos el customerId de la lista
                .filter( x -> (x.getProduct().getIndProduct() == 1 && x.getCustomer().getTypeCustomer() == 1) && (x.getProduct().getId().equals(a.getProductId())) ) // Buscamos si tiene una cuenta bancaria y es cliente personal
                .hasElements()
                .flatMap( v -> {
                    if (v){
                        return Mono.error(new RuntimeException("El cliente personal no puede tener mas de una cuenta bancaria"));
                    }else{
                        return this.findAllWithDetail()
                                .filter( x -> x.getCustomerId().equals(a.getCustomerId())) // Buscamos el customerId de la lista
                                .filter( x -> (x.getProduct().getIndProduct() == 2 && x.getCustomer().getTypeCustomer() == 1) && (x.getProduct().getId().equals(a.getProductId())) ) // Buscamos si tiene un credito y es cliente personal
                                .hasElements()
                                .flatMap( w -> {
                                   if (w){
                                       return Mono.error(new RuntimeException("El cliente personal no puede tener mas de un credito"));
                                   }else{
                                       return productClient.getProduct(a.getProductId())
                                               .filter( x -> (x.getIndProduct() == 1) ) // Validar si el producto es PASIVO
                                               .filter( x -> (x.getTypeProduct() == 1 || x.getTypeProduct() == 3) ) // Validar si es cuenta de ahorros o plazo fijo
                                               .hasElement()
                                               .flatMap( zz -> {
                                                   return customerClient.getCustomer(a.getCustomerId())
                                                           .filter( (x -> x.getTypeCustomer() == 2) ) // Validar si el customerId es empresarial
                                                           .hasElement()
                                                           .flatMap( yy -> {
                                                               return customerClient.getCustomer(a.getCustomerId()).flatMap(customerToSend -> {

                                                                   return productClient.getProduct(a.getProductId()).flatMap(pToSend -> {
                                                                       if ( zz && yy && customerToSend.getTypeCustomer() == 2){
                                                                           return Mono.error(new RuntimeException("El cliente empresarial no puede tener una cuenta de ahorros o plazo fijo"));
                                                                       }else if(customerToSend.getTypeCustomer() == 1 || customerToSend.getTypeCustomer() == 2){
                                                                           if (a.getCreditActually().compareTo(BigDecimal.ZERO)<=0) {
                                                                               a.setCreditActually(BigDecimal.ZERO);
                                                                           }
                                                                           return expiredDebt(a);
                                                                       } else if (customerToSend.getTypeCustomer() == 3) {
                                                                           Boolean b = pToSend.getAmountPerDay().compareTo(BigDecimal.ZERO)>0 && pToSend.getAmountPerMonth().compareTo(BigDecimal.ZERO)>0;
                                                                           return personalVipEmpresaPymeValidation(a, customerToSend.getTypeCustomer(), 1, b); // Cuenta de ahorro para Personal Vip
                                                                       } else {

                                                                           return personalVipEmpresaPymeValidation(a, customerToSend.getTypeCustomer(), 2 , true); // Cuenta corriente Empresarial Pyme
                                                                       }
                                                                   });
                                                                   });
                                                           });
                                               });
                                   }
                                });
                    }
                });
    }

    public Mono<Account> personalVipEmpresaPymeValidation(Account t, int typeCostumerToValidate, int typeProductToValidate, Boolean f) {
        return hasCreditCard(t).filter(btc -> btc.equals(Boolean.TRUE))
                .hasElements()
                .flatMap(bTarjetaCredito -> {
                    if (Boolean.TRUE.equals(bTarjetaCredito)) {
                        return productClient.getProduct(t.getProductId())
                                .filter( x -> (x.getTypeProduct() == typeProductToValidate && f)
                                        || x.getTypeProduct() == 6)
                                .hasElement()
                                .flatMap( bp -> {
                                    return customerClient.getCustomer(t.getCustomerId())
                                            .filter( (x -> x.getTypeCustomer() == typeCostumerToValidate) )
                                            .hasElement()
                                            .flatMap( bc -> {
                                                if ( bp  && bc ){
                                                    if (t.getCreditActually().compareTo(BigDecimal.ZERO)<=0) {
                                                        t.setCreditActually(BigDecimal.ZERO);
                                                    }
                                                    // Comision 0 para personal vip y empresarial pyme
                                                    t.setCommission(BigDecimal.ZERO);
                                                    return expiredDebt(t);
                                                    //
                                                }else{
                                                    return Mono.error(new RuntimeException("No se pudo crear una cuenta de ahorro para Personal VIP, no cumple las condiciones"));
                                                }
                                            });
                                });
                    } else {
                        return productClient.getProduct(t.getProductId())
                                .filter( x -> ( x.getTypeProduct() == 6 ))
                                .hasElement()
                                .flatMap( bp -> {
                                    if (bp) {
                                        if (t.getCreditActually().compareTo(BigDecimal.ZERO)<=0) {
                                            t.setCreditActually(BigDecimal.ZERO);
                                        }
                                        // Comision 0 para personal vip y empresarial pyme
                                        t.setCommission(BigDecimal.ZERO);
                                        return expiredDebt(t);
                                    } else {
                                        return Mono.error(new RuntimeException("Solo puede crearse cuenta de ahorro para Personal Vip, si tiene tarjeta de credito"));
                                    }
                                });
                    }
                });
    }

    public Flux<Boolean> hasCreditCard(Account t) {
        //account - productId - evaluarProductId - boolean
        return findAll().filter(trans -> trans.getCustomerId().equalsIgnoreCase(t.getCustomerId()))
                .flatMap(trans -> {
                    return productClient.getProduct(trans.getProductId()).filter(prod -> prod.getTypeProduct() == 6)
                            .hasElement();
                });
    }

    @Override
    public Mono<Account> findById(String id) {
        logger.info("Executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Account> findByIdWithDetail(String id) {
        logger.info("Executing findByIdWithDetail method");
        return repository.findById(id)
                .flatMap( x -> {
                    return customerClient.getCustomer(x.getCustomerId())
                            .flatMap( y -> {
                                return productClient.getProduct(x.getProductId())
                                        .flatMap( z -> {
                                            return depositClient.getDeposits()
                                                    .filter( val1 -> val1.getAccountId().equals(x.getId()))
                                                    .collectList()
                                                    .flatMap( w -> {
                                                        return withdrawalClient.getWithdrawals()
                                                                .filter( val2 -> val2.getAccountId().equals(x.getId()))
                                                                .collectList()
                                                                .flatMap( v -> {
                                                                    return paymentClient.getPayments()
                                                                            .filter( val3 -> val3.getAccountId().equals(x.getId()))
                                                                            .collectList()
                                                                            .flatMap( t -> {
                                                                               return purchaseClient.getPurchases()
                                                                                       .filter( val4 -> val4.getAccountId().equals(x.getId()))
                                                                                       .collectList()
                                                                                       .flatMap( o -> {
                                                                                           return signatoryClient.getSignatories()
                                                                                                   .filter( val5 -> val5.getAccountId().equals(x.getId()))
                                                                                                   .collectList()
                                                                                                   .flatMap( p -> {
                                                                                                       this.setAccountValues(x, y, z, w, v, o, t, p);
                                                                                                       return Mono.just(x);
                                                                                                   });
                                                                                       });
                                                                            });
                                                                });
                                                    });
                                        });
                            });
                });
    }

    @Override
    public Mono<Account> update(Account a, String id) {
        logger.info("Executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setProductId(a.getProductId());
                    x.setAccountNumber(a.getAccountNumber());
                    x.setAccountNumberInt(a.getAccountNumberInt());
                    x.setMovementLimits(a.getMovementLimits());
                    x.setMovementActually(a.getMovementActually());
                    x.setCreditLimits(a.getCreditLimits());
                    x.setCreditActually(a.getCreditActually());
                    x.setMovementDate(a.getMovementDate());
                    x.setCardNumber(a.getCardNumber());
                    x.setMaxAmountTransaction(a.getMaxAmountTransaction());
                    x.setCurrentNumberTransaction(a.getCurrentNumberTransaction());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Account> delete(String id) {
        logger.info("Executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }

    private void setAccountValues(Account x, Customer y, Product z, List<Deposit> w, List<Withdrawal> v, List<Purchase> t, List<Payment> o, List<Signatories> p) {
        x.setCustomer(y);
        x.setProduct(z);
        x.setDeposits(new ArrayList<>(w));
        x.setWithdrawals(new ArrayList<>(v));
        x.setPayments(new ArrayList<>(o));
        x.setPurchases(new ArrayList<>(t));
        x.setSignatories(new ArrayList<>(p));
    }

    private Mono<Account> expiredDebt (Account account) {
        return findAllWithDetail().filter(a -> a.getCustomerId().equalsIgnoreCase(account.getCustomerId()))
                .filter(a -> a.getProduct().getIndProduct() == 1)
                .filter(a -> a.getExpiredDebt() == "S")
                .hasElements()
                .flatMap(deuda -> {
                    if(deuda) {
                        return Mono.error(new RuntimeException("Tiene una deuda vencida"));
                    } else {
                        return repository.save(account);
                    }

                });
    }

    @Override
    public Flux<Account> lastTenMovements() {
        return repository.findAll()
                .flatMap( trans -> customerClient.getCustomer(trans.getCustomerId())
                        .flatMapMany( customer -> {
                            return productClient.getProduct(trans.getProductId())
                                    .flatMapMany( product -> depositClient.getDeposits()
                                            .filter(x -> x.getAccountId().equals(trans.getId()))
                                            .collectList()
                                            .flatMapMany((deposit -> {
                                                return withdrawalClient.getWithdrawals()
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
                                                                                    return signatoryClient.getSignatories()
                                                                                            .filter(o -> o.getAccountId().equals(trans.getId()))
                                                                                            .collectList()
                                                                                            .flatMapMany(signatories -> {
                                                                                                List<DateInterface> x= new ArrayList<>();
                                                                                                x.addAll(deposit);
                                                                                                x.addAll(withdrawals);
                                                                                                x.addAll(payments);
                                                                                                x.addAll(purchases);
                                                                                                x.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                                                                                                logger.info(x.toString());
                                                                                                return Flux.fromIterable(x).take(10).collectList().flatMapMany(to -> {
                                                                                                    trans.setMovements(to);
                                                                                                    return Flux.just(trans);
                                                                                                });
                                                                                            });
                                                                                });

                                                                    } ));
                                                        } ));
                                            })));
                        }));
    }

}
