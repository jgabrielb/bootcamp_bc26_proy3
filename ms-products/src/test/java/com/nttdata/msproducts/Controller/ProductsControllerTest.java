package com.nttdata.msproducts.Controller;

import com.nttdata.msproducts.model.Products;
import com.nttdata.msproducts.service.ProductsService;
import com.nttdata.msproducts.service.impl.ProductsServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
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
@WebFluxTest(controllers = ProductsController.class)
@Import(ProductsServiceImpl.class)
class ProductsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    ProductsService productService;

    @Test
    void createDepositTest() {
        Products productMono = Products.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Mockito.when(productService.save(Mockito.any())).thenReturn(Mono.just(productMono));

        webTestClient.post().uri("/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(productMono))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(productService,times(1)).save(productMono);
    }

    @Test
    void updateDepositTest() {
        Products productMono = Products.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();
        String id = "6767668789fds9";
        Mockito.when(productService.update(productMono, id))
                .thenReturn(Mono.just(productMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/products/update/{id}")
                        .build(id))
                .body(Mono.just(productMono), Products.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(productService,times(1)).update(productMono,id);
    }

    @Test
    void findAll() {
        Products productMono = Products.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Mockito.when(productService.findAll()).thenReturn(Flux.just(productMono));

        webTestClient.get()
                .uri("/products/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Products.class);

        Mockito.verify(productService,times(1)).findAll();
    }

    @Test
    void FindById() {

        Products product = new Products();
        product.setId("12buhvg24uhjknv2");
        product.setIndProduct(1);
        product.setDescIndProduct("cuenta bancaria");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Mockito.when(productService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(product));

        webTestClient.get()
                .uri("/products/find/{id}","12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.indProduct").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.indProduct").isEqualTo(1)
                .jsonPath("$.descIndProduct").isEqualTo("cuenta bancaria")
                .jsonPath("$.typeProduct").isEqualTo(1)
                .jsonPath("$.descTypeProduct").isEqualTo("cuenta de ahorro");


        Mockito.verify(productService,times(1)).findById("12buhvg24uhjknv2");
    }

    @Test
    void Delete() {

        Products productMono = Products.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();
        String id = "6767668789fds9";

        Mockito.when(productService.delete(id))
                .thenReturn(Mono.just(productMono));

        webTestClient.delete().uri("/products/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(productService,times(1)).delete(id);

    }
}