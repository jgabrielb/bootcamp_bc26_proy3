package com.nttdata.mssignatories.controller;

import com.nttdata.mssignatories.model.Signatories;
import com.nttdata.mssignatories.service.SignatoriesService;
import com.nttdata.mssignatories.service.impl.SignatoriesServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SignatoriesController.class)
@Import(SignatoriesServiceImpl.class)
class SignatoriesControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SignatoriesService signatoryService;

    @Test
    void createSignatoriesTest() {
        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("62ce2098459d957a015af234")
                .build();
        Mockito.when(signatoryService.save(signatoryMono)).thenReturn(Mono.just(signatoryMono));
        webTestClient.post().uri("/signatories/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(signatoryMono))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(signatoryService,times(1)).save(signatoryMono);
    }

    @Test
    void updateSignatoriesTest() {
        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "unhb2342342342";
        Mockito.when(signatoryService.update(signatoryMono, id))
                .thenReturn(Mono.just(signatoryMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/signatories/update/{id}")
                        .build(id))
                .body(Mono.just(signatoryMono), Signatories.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(signatoryService,times(1)).update(signatoryMono,id);
    }

    @Test
    void findAll() {

        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "unhb2342342342";

        Mockito.when(signatoryService.findAll()).thenReturn(Flux.just(signatoryMono));

        webTestClient.get()
                .uri("/signatories/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Signatories.class);

        Mockito.verify(signatoryService,times(1)).findAll();
    }

    @Test
    void FindById() {

        Signatories signatory = new Signatories();
        signatory.setId("unhb2342342342");
        signatory.setFirstName("Juan");
        signatory.setLastName("fernandez");
        signatory.setDocNumber("98765432");
        signatory.setAccountId("62ce2098459d957a015af234");

        Mockito.when(signatoryService.findById("unhb2342342342")).thenReturn(Mono.just(signatory));

        webTestClient.get()
                .uri("/signatories/find/{id}","unhb2342342342")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo("unhb2342342342")
                .jsonPath("$.firstName").isEqualTo("Juan")
                .jsonPath("$.lastName").isEqualTo("fernandez")
                .jsonPath("$.docNumber").isEqualTo("98765432")
                .jsonPath("$.accountId").isEqualTo("62ce2098459d957a015af234");


        Mockito.verify(signatoryService,times(1)).findById("unhb2342342342");
    }

    @Test
    void Delete() {

        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "unhb2342342342";

        Mockito.when(signatoryService.delete(id))
                .thenReturn(Mono.just(signatoryMono));

        webTestClient.delete().uri("/signatories/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(signatoryService,times(1)).delete(id);

    }
}