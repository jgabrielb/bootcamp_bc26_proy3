package com.nttdata.mspayments.controller;

import com.nttdata.mspayments.model.Payments;
import com.nttdata.mspayments.service.PaymentsService;
import com.nttdata.mspayments.service.impl.PaymentsServiceImpl;
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
@WebFluxTest(controllers = PaymentsController.class)
@Import(PaymentsServiceImpl.class)
class PaymentsControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PaymentsService paymentService;

    @Test
    void createPaymentTest() {
        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Mockito.when(paymentService.save(paymentMono)).thenReturn(Mono.just(paymentMono));

        webTestClient.post().uri("/payments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paymentMono))
                .exchange()
                .expectStatus()
                .isCreated().expectBody();

        Mockito.verify(paymentService,times(1)).save(paymentMono);
    }

    @Test
    void updatePaymentTest() {
        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("234234jnjk2345")
                .build();
        String id = "6767668789fds9";
        Mockito.when(paymentService.update(paymentMono, id))
                .thenReturn(Mono.just(paymentMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/payments/update/{id}")
                        .build(id))
                .body(Mono.just(paymentMono), Payments.class)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(paymentService,times(1)).update(paymentMono,id);
    }

    @Test
    void findAll() {

        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("234234jnjk2345")
                .build();

        Mockito.when(paymentService.findAll()).thenReturn(Flux.just(paymentMono));
        webTestClient.get()
                .uri("/payments/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Payments.class);

        Mockito.verify(paymentService,times(1)).findAll();
    }

    @Test
    void FindById() {

        Payments payment = new Payments();
        payment.setId("12buhvg24uhjknv2");
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentAmount(BigDecimal.valueOf(200));
        payment.setDescription("Servicio Claro");
        payment.setCurrency("PEN");
        payment.setAccountId("2323423424");

        Mockito.when(paymentService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(payment));

        webTestClient.get()
                .uri("/payments/find/{id}","12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paymentDate").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.paymentDate").isEqualTo("2022-07-20")
                .jsonPath("$.paymentAmount").isEqualTo(BigDecimal.valueOf(200))
                .jsonPath("$.description").isEqualTo("Servicio Claro")
                .jsonPath("$.currency").isEqualTo("PEN")
                .jsonPath("$.accountId").isEqualTo("2323423424");


        Mockito.verify(paymentService,times(1)).findById("12buhvg24uhjknv2");
    }

    @Test
    void Delete() {

        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("234234jnjk2345")
                .build();
        String id = "6767668789fds9";

        Mockito.when(paymentService.delete(id))
                .thenReturn(Mono.just(paymentMono));

        webTestClient.delete().uri("/payments/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(paymentService,times(1)).delete(id);

    }
}