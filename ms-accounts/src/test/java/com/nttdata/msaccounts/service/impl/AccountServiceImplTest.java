package com.nttdata.msaccounts.service.impl;

import com.nttdata.msaccounts.client.*;
import com.nttdata.msaccounts.model.*;
import com.nttdata.msaccounts.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private DepositClient depositClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private PurchaseClient purchaseClient;

    @Mock
    private SignatoryClient signatoryClient;

    @Mock
    private WithdrawalClient withDrawalClient;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;


    @Test
    void findAllWithDetail(){
        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Deposit deposit = Deposit.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("84374234y743123")
                .build();

        Withdrawal withdrawal = Withdrawal.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("84374234y743123")
                .build();

        Signatories signatory = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("8797897797")
                .accountId("84374234y743123")
                .build();

        Payment payment = Payment.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("demo payment")
                .currency("PEN")
                .accountId("234234jnjk2345")
                .build();

        Purchase purchase = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("demo purchase")
                .currency("PEN")
                .accountId("34242423234")
                .build();

        Account accountMono = Account.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .build();

        Mockito.when(accountRepository.findAll()).thenReturn(Flux.just(accountMono));
        Mockito.when(customerClient.getCustomer(customer.getId())).thenReturn(Mono.just(customer));
        Mockito.when(productClient.getProduct(product.getId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposits()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithdrawals()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayments()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchases()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatories()).thenReturn(Flux.just(signatory));


        assertDoesNotThrow(() -> accountServiceImpl.findAllWithDetail()
                .subscribe(response -> {
                    assertEquals(accountMono.getAccountNumber(), response.getAccountNumber());
                }));
    }

    @Test
    void updateAccountTest() {

        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Account accountMono = Account.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .deposits(new ArrayList<>())
                .withdrawals(new ArrayList<>())
                .payments(new ArrayList<>())
                .purchases(new ArrayList<>())
                .signatories(new ArrayList<>())
                .build();
        String id = "6767668789fds9";

        Mockito.when(accountRepository.findById(id)).thenReturn(Mono.just(accountMono));
        Mockito.when(accountRepository.save(accountMono)).thenReturn(Mono.just(accountMono));

        assertDoesNotThrow(() -> accountServiceImpl.update(accountMono,id)
                .subscribe(response -> {
                    assertEquals(accountMono.getAccountNumber(), response.getAccountNumber());
                }));
    }

    @Test
    void findAll() {

        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Account accountMono = Account.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .deposits(new ArrayList<>())
                .withdrawals(new ArrayList<>())
                .payments(new ArrayList<>())
                .purchases(new ArrayList<>())
                .signatories(new ArrayList<>())
                .build();

        Mockito.when(accountRepository.findAll()).thenReturn(Flux.just(accountMono));

        assertDoesNotThrow(() -> accountServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(accountMono.getAccountNumber(), response.getAccountNumber());
                }));
    }

    @Test
    void FindById() {

        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();
        Account accountMono = Account.builder()
                .id("2346723847262")
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .deposits(new ArrayList<>())
                .withdrawals(new ArrayList<>())
                .payments(new ArrayList<>())
                .purchases(new ArrayList<>())
                .signatories(new ArrayList<>())
                .build();

        Mockito.when(accountRepository.findById("2346723847262")).thenReturn(Mono.just(accountMono));

        assertDoesNotThrow(() -> accountServiceImpl.findById(accountMono.getId())
                .subscribe(response -> {
                    assertEquals(accountMono.getAccountNumber(), response.getAccountNumber());
                }));
    }

    @Test
    void Delete() {
        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Account accountMono = Account.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .build();
        String id = "6767668789fds9";

        Mockito.when(accountRepository.findById("6767668789fds9")).thenReturn(Mono.just(accountMono));
        Mockito.when(accountRepository.delete(accountMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> accountServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Account(), response);
                }));

    }

    @Test
    void findByIdWithDetail(){
        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Deposit deposit = Deposit.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("84374234y743123")
                .build();

        Withdrawal withdrawal = Withdrawal.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("84374234y743123")
                .build();

        Signatories signatory = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("8797897797")
                .accountId("84374234y743123")
                .build();

        Payment payment = Payment.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("demo payment")
                .currency("PEN")
                .accountId("234234jnjk2345")
                .build();

        Purchase purchase = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("demo purchase")
                .currency("PEN")
                .accountId("34242423234")
                .build();

        Account accountMono = Account.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .build();

        Mockito.when(accountRepository.findById(accountMono.getId())).thenReturn(Mono.just(accountMono));
        Mockito.when(customerClient.getCustomer(customer.getId())).thenReturn(Mono.just(customer));
        Mockito.when(productClient.getProduct(product.getId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposits()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithdrawals()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayments()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchases()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatories()).thenReturn(Flux.just(signatory));


        assertDoesNotThrow(() -> accountServiceImpl.findByIdWithDetail(accountMono.getId())
                .subscribe(response -> {
                    assertEquals(accountMono.getAccountNumber(), response.getAccountNumber());
                }));
    }

    @Test
    void saveAccount(){

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(2)
                .descIndProduct("pasivo")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Customer customer = Customer.builder()
                .id("2854445425")
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();


        Account accountMono = Account.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .accountNumber("38748398273492734")
                .accountNumberInt("13123412")
                .movementLimits(5)
                .movementActually(1)
                .creditLimits(BigDecimal.valueOf(100))
                .creditActually(BigDecimal.valueOf(2600))
                .commission(BigDecimal.valueOf(5))
                .movementDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .build();

        Mockito.when(productClient.getProduct(product.getId())).thenReturn(Mono.just(product));
        Mockito.when(customerClient.getCustomer(customer.getId())).thenReturn(Mono.just(customer));
        Mockito.when(accountRepository.findAll()).thenReturn(Flux.just(accountMono));


        assertDoesNotThrow(() -> accountServiceImpl.save(accountMono)
                .subscribe(response -> {
                    assertEquals(accountMono.getAccountNumber(), response.getAccountNumber());
                }));
    }
}