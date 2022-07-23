package com.nttdata.msdeposits.controller;

import com.nttdata.msdeposits.model.Deposits;
import com.nttdata.msdeposits.service.DepositsService;
import com.nttdata.msdeposits.service.impl.DepositsServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
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
@WebFluxTest(controllers = DepositsController.class)
@Import(DepositsServiceImpl.class)
class DepositsControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    DepositsService depositService;

    @Test
    void createDepositTest() {
        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();


        Mockito.when(depositService.save(depositMono)).thenReturn(Mono.just(depositMono));
        webTestClient.post().uri("/deposits/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(depositMono))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(depositService,times(1)).save(depositMono);
    }

    @Test
    void updateDepositTest() {
        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "unhb2342342342";


        Mockito.when(depositService.update(depositMono, id))
                .thenReturn(Mono.just(depositMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/deposits/update/{id}")
                        .build(id))
                .body(Mono.just(depositMono), Deposits.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(depositService,times(1)).update(depositMono,id);
    }

    @Test
    void findAll() {
        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Deposits deposit = new Deposits();
        BeanUtils.copyProperties(depositMono,deposit);

        Mockito.when(depositService.findAll()).thenReturn(Flux.just(deposit));

        webTestClient.get()
                .uri("/deposits/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Deposits.class);

        Mockito.verify(depositService,times(1)).findAll();
    }

    @Test
    void FindById() {

        Deposits deposit = new Deposits();
        deposit.setId("unhb2342342342");
        deposit.setDepositDate(LocalDate.now());
        deposit.setDepositAmount(BigDecimal.valueOf(200));
        deposit.setCurrency("PEN");
        deposit.setAccountId("62ce2098459d957a015af234");

        Mockito.when(depositService.findById("unhb2342342342")).thenReturn(Mono.just(deposit));

        webTestClient.get()
                .uri("/deposits/find/{id}","unhb2342342342")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.depositDate").isNotEmpty()
                .jsonPath("$.id").isEqualTo("unhb2342342342")
                .jsonPath("$.depositDate").isEqualTo("2022-07-20")
                .jsonPath("$.depositAmount").isEqualTo(BigDecimal.valueOf(200))
                .jsonPath("$.currency").isEqualTo("PEN")
                .jsonPath("$.accountId").isEqualTo("62ce2098459d957a015af234");


        Mockito.verify(depositService,times(1)).findById("unhb2342342342");
    }

    @Test
    void Delete() {

        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        String id = "unhb2342342342";
        Mockito.when(depositService.delete(id))
                .thenReturn(Mono.just(depositMono));

        webTestClient.delete().uri("/deposits/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(depositService,times(1)).delete(id);

    }
}