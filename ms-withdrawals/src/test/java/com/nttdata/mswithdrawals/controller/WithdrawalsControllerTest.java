package com.nttdata.mswithdrawals.controller;

import com.nttdata.mswithdrawals.model.Withdrawals;
import com.nttdata.mswithdrawals.service.WithdrawalsService;
import com.nttdata.mswithdrawals.service.impl.WithdrawalsServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = WithdrawalsController.class)
@Import(WithdrawalsServiceImpl.class)
class WithdrawalsControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WithdrawalsService withdrawalService;

    @Test
    void createWithDrawalTest() {
        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        Mockito.when(withdrawalService.save(Mockito.any())).thenReturn(Mono.just(withdrawalMono));
        webTestClient.post().uri("/withdrawals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(withdrawalMono))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(withdrawalService,times(1)).save(withdrawalMono);
    }

    @Test
    void updateWithDrawalTest() {
        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "6767668789fds9";
        Mockito.when(withdrawalService.update(withdrawalMono, id))
                .thenReturn(Mono.just(withdrawalMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/withdrawals/update/{id}")
                        .build(id))
                .body(Mono.just(withdrawalMono), Withdrawals.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(withdrawalService,times(1)).update(withdrawalMono,id);
    }

    @Test
    void findAll() {

        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Mockito.when(withdrawalService.findAll()).thenReturn(Flux.just(withdrawalMono));

        webTestClient.get()
                .uri("/withdrawals/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Withdrawals.class);

        Mockito.verify(withdrawalService,times(1)).findAll();
    }

    @Test
    void FindById() {

        Withdrawals signatory = new Withdrawals();
        signatory.setId("12buhvg24uhjknv2");
        signatory.setWithdrawalsDate(LocalDate.now());
        signatory.setWithdrawalsAmount(BigDecimal.valueOf(2000));
        signatory.setCurrency("PEN");
        signatory.setAccountId("62ce2098459d957a015af234");

        Mockito.when(withdrawalService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(signatory));

        webTestClient.get()
                .uri("/withdrawals/find/{id}","12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.withdrawalsDate").isEqualTo("2022-07-20")
                .jsonPath("$.withdrawalsAmount").isEqualTo(BigDecimal.valueOf(2000))
                .jsonPath("$.currency").isEqualTo("PEN")
                .jsonPath("$.accountId").isEqualTo("62ce2098459d957a015af234");


        Mockito.verify(withdrawalService,times(1)).findById("12buhvg24uhjknv2");
    }

    @Test
    void Delete() {

        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(withdrawalService.delete(id))
                .thenReturn(Mono.just(withdrawalMono));

        webTestClient.delete().uri("/withdrawals/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(withdrawalService,times(1)).delete(id);

    }
}