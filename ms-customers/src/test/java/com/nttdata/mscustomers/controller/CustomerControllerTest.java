package com.nttdata.mscustomers.controller;

import com.nttdata.mscustomers.model.Customer;
import com.nttdata.mscustomers.repository.CustomerRepository;
import com.nttdata.mscustomers.service.CustomerService;
import com.nttdata.mscustomers.service.impl.CustomerServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CustomerController.class)
@Import(CustomerServiceImpl.class)

class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private WebTestClient webTestClient;
    @Test
    void createCustomerTest() {
        Customer customerMono = Customer.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();
        Mockito.when(customerService.save(Mockito.any())).thenReturn(Mono.just(customerMono));

        webTestClient.post().uri("/customers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customerMono))
                .exchange()
                .expectStatus()
                .isCreated().expectBody();


    }

    @Test
    void updateCustomerTest() {
        Customer customerMono = Customer.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();
        String id = "unhb2342342342";

        Mockito.when(customerService.update(customerMono, id)).thenReturn(Mono.just(customerMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/customers/update/{id}")
                        .build(id))
                .body(Mono.just(customerMono), Customer.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(customerService,times(1)).update(customerMono,id);
    }

    @Test
    void findAll() {
        Customer customerMono = Customer.builder()
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Mockito.when(customerService.findAll()).thenReturn(Flux.just(customerMono));

        webTestClient.get()
                .uri("/customers/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Customer.class);

        Mockito.verify(customerService,times(1)).findAll();
    }

    @Test
    void FindById() {

        Customer customer = new Customer();
        customer.setId("12buhvg24uhjknv2");
        customer.setFirstName("Juan");
        customer.setLastName("Fernandez");
        customer.setDocNumber("23424234234");
        customer.setTypeCustomer(1);
        customer.setDescTypeCustomer("personal");

        Mockito.when(customerService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(customer));

        webTestClient.get()
                .uri("/customers/find/{id}","12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.firstName").isEqualTo("Juan")
                .jsonPath("$.lastName").isEqualTo("Fernandez")
                .jsonPath("$.docNumber").isEqualTo("23424234234")
                .jsonPath("$.typeCustomer").isEqualTo(1)
                .jsonPath("$.descTypeCustomer").isEqualTo("personal");

        Mockito.verify(customerService,times(1)).findById("12buhvg24uhjknv2");

    }

    @Test
    void Delete() {

        Customer customerMono = Customer.builder()
                .firstName("Juan")
                .lastName("Fernandez")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        String id = "unhb2342342342";
        Mockito.when(customerService.delete(id))
                .thenReturn(Mono.just(customerMono));

        webTestClient.delete().uri("/customers/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(customerService,times(1)).delete(id);

    }
}