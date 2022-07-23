package com.nttdata.mspurchase.controller;

import com.nttdata.mspurchase.model.Purchase;
import com.nttdata.mspurchase.service.PurchaseService;
import com.nttdata.mspurchase.service.impl.PurchaseServiceImpl;
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
@WebFluxTest(controllers = PurchaseController.class)
@Import(PurchaseServiceImpl.class)
class PurchaseControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    PurchaseService purchaseService;

    @Test
    public void createPurchaseTest() {
        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        Mockito.when(purchaseService.save(purchaseMono)).thenReturn(Mono.just(purchaseMono));
        webTestClient.post().uri("/purchase/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(purchaseMono))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(purchaseService,times(1)).save(purchaseMono);
    }

    @Test
    public void updatePurchaseTest() {
        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "6767668789fds9";
        Mockito.when(purchaseService.update(purchaseMono, id))
                .thenReturn(Mono.just(purchaseMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/purchase/update/{id}")
                        .build(id))
                .body(Mono.just(purchaseMono), Purchase.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(purchaseService,times(1)).update(purchaseMono,id);
    }

    @Test
    public void findAll() {

        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("342873574h")
                .build();

        Mockito.when(purchaseService.findAll()).thenReturn(Flux.just(purchaseMono));

        webTestClient.get()
                .uri("/purchase/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Purchase.class);

        Mockito.verify(purchaseService,times(1)).findAll();
    }

    @Test
    public void FindById() {

        Purchase purchase = new Purchase();
        purchase.setId("12buhvg24uhjknv2");
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setPurchaseAmount(BigDecimal.valueOf(100));
        purchase.setDescription("Servicio Claro");
        purchase.setCurrency("PEN");
        purchase.setAccountId("678676898wdfs");

        Mockito.when(purchaseService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(purchase));

        webTestClient.get()
                .uri("/purchase/find/{id}","12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.purchaseDate").isEqualTo("2022-07-20")
                .jsonPath("$.purchaseAmount").isEqualTo(BigDecimal.valueOf(100))
                .jsonPath("$.description").isEqualTo("Servicio Claro")
                .jsonPath("$.currency").isEqualTo("PEN")
                .jsonPath("$.accountId").isEqualTo("678676898wdfs");


        Mockito.verify(purchaseService,times(1)).findById("12buhvg24uhjknv2");
    }

    @Test
    public void Delete() {

        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("342873574h")
                .build();
        String id = "6767668789fds9";

        Mockito.when(purchaseService.delete(id))
                .thenReturn(Mono.just(purchaseMono));

        webTestClient.delete().uri("/purchase/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(purchaseService,times(1)).delete(id);

    }
}