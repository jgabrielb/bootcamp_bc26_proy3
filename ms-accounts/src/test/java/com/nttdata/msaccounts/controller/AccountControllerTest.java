package com.nttdata.msaccounts.controller;

import com.nttdata.msaccounts.model.Account;
import com.nttdata.msaccounts.model.Customer;
import com.nttdata.msaccounts.model.Product;
import com.nttdata.msaccounts.service.AccountService;
import com.nttdata.msaccounts.service.impl.AccountServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AccountController.class)
@Import(AccountServiceImpl.class)
class AccountControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @Test
    void findAll() {

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
                .customer(new Customer())
                .product(new Product())
                .build();

        Mockito.when(accountService.findAll()).thenReturn(Flux.just(accountMono));

        webTestClient.get()
                .uri("/accounts/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class);

        Mockito.verify(accountService, times(1)).findAll();
    }

    @Test
    void FindById() {

        Account accountMono = Account.builder()
                .id("12buhvg24uhjknv2")
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
                .customer(new Customer())
                .product(new Product())
                .build();

        Mockito.when(accountService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(accountMono));

        webTestClient.get()
                .uri("/accounts/find/{id}", "12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.customerId").isEqualTo("23424242345fdd")
                .jsonPath("$.productId").isEqualTo("242342j3nji234")
                .jsonPath("$.accountNumber").isEqualTo("38748398273492734")
                .jsonPath("$.accountNumberInt").isEqualTo("13123412")
                .jsonPath("$.movementLimits").isEqualTo(5)
                .jsonPath("$.movementActually").isEqualTo(1)
                .jsonPath("$.creditLimits").isEqualTo(BigDecimal.valueOf(100))
                .jsonPath("$.creditActually").isEqualTo(BigDecimal.valueOf(2600))
                .jsonPath("$.commission").isEqualTo(BigDecimal.valueOf(5))
                .jsonPath("$.movementDate").isEqualTo("2022-07-20")
                .jsonPath("$.customer").isEqualTo(new Customer())
                .jsonPath("$.product").isEqualTo(new Product());

        Mockito.verify(accountService, times(1)).findById("12buhvg24uhjknv2");
    }

    @Test
    void Delete() {

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
                .customer(new Customer())
                .product(new Product())
                .deposits(new ArrayList<>())
                .withdrawals(new ArrayList<>())
                .payments(new ArrayList<>())
                .purchases(new ArrayList<>())
                .signatories(new ArrayList<>())
                .build();
        String id = "6767668789fds9";

        Mockito.when(accountService.findById(id)).thenReturn(Mono.just(accountMono));
        Mockito.when(accountService.delete(id)).thenReturn(Mono.just(accountMono));

        webTestClient.delete().uri("/accounts/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(accountService,times(1)).delete(id);

    }

    @Test
    void updateAccountTest() {
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
                .build();
        String id = "6767668789fds9";

        Mockito.when(accountService.findById(id)).thenReturn(Mono.just(accountMono));
        Mockito.when(accountService.update(accountMono, id)).thenReturn(Mono.just(accountMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/accounts/update/{id}")
                        .build(id))
                .body(Mono.just(accountMono), Account.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(accountService,times(1)).update(accountMono,id);
    }

    @Test
    void findAllWithDetail() {

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
                .customer(new Customer())
                .product(new Product())
                .build();

        Mockito.when(accountService.findAllWithDetail()).thenReturn(Flux.just(accountMono));

        webTestClient.get()
                .uri("/accounts/findAllWithDetail")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class);

        Mockito.verify(accountService, times(1)).findAllWithDetail();
    }

    @Test
    void findByIdWithDetail() {

        Account account = new Account();
        account.setId("62ce2098459d957a015af234");
        account.setCustomerId("2854445425");
        account.setProductId("83457346534534");
        account.setAccountNumber("9946548947640");
        account.setAccountNumberInt("00799401654894764075");
        account.setMovementLimits(1);
        account.setMovementActually(5);
        account.setCreditLimits(BigDecimal.valueOf(100));
        account.setCreditActually(BigDecimal.valueOf(7500));
        account.setCommission(BigDecimal.valueOf(5));
        account.setMovementDate(LocalDate.parse("2022-07-20"));
        account.setCustomer(new Customer());
        account.setProduct(new Product());


        Mockito.when(accountService.findByIdWithDetail("62ce2098459d957a015af234")).thenReturn(Mono.just(account));

        webTestClient.get()
                .uri("/accounts/findWithDetailsById/{id}", "62ce2098459d957a015af234")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo("62ce2098459d957a015af234")
                .jsonPath("$.customerId").isEqualTo("2854445425")
                .jsonPath("$.productId").isEqualTo("83457346534534")
                .jsonPath("$.accountNumber").isEqualTo("9946548947640")
                .jsonPath("$.accountNumberInt").isEqualTo("00799401654894764075")
                .jsonPath("$.movementLimits").isEqualTo(1)
                .jsonPath("$.movementActually").isEqualTo(5)
                .jsonPath("$.creditLimits").isEqualTo(BigDecimal.valueOf(100))
                .jsonPath("$.creditActually").isEqualTo(BigDecimal.valueOf(7500))
                .jsonPath("$.commission").isEqualTo(BigDecimal.valueOf(5))
                .jsonPath("$.movementDate").isEqualTo("2022-07-20")
                .jsonPath("$.customer").isEqualTo(new Customer())
                .jsonPath("$.product").isEqualTo(new Product());

        Mockito.verify(accountService, times(1)).findByIdWithDetail("62ce2098459d957a015af234");
    }

    @Test
    void createAccountTest() {
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
                .build();
        Mockito.when(accountService.findAllWithDetail()).thenReturn(Flux.just(accountMono));
        Mockito.when(accountService.save(accountMono)).thenReturn(Mono.just(accountMono));
        webTestClient.post().uri("/accounts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(accountMono))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(accountService,times(1)).save(accountMono);
    }
}