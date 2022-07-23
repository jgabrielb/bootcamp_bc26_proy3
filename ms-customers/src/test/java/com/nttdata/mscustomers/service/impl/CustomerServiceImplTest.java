package com.nttdata.mscustomers.service.impl;

import com.nttdata.mscustomers.model.Customer;
import com.nttdata.mscustomers.repository.CustomerRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @Test
    void createCustomerTest() {
        Customer customerMono = Customer.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(Mono.just(customerMono));

        assertDoesNotThrow(() -> customerServiceImpl.save(customerMono)
                .subscribe(response -> {
                    assertEquals(customerMono.getLastName(), response.getLastName());
                }));

    }

    @Test
    void updateCustomerTest() {
        Customer customerMono = Customer.builder()
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();
        String id = "unhb2342342342";

        Mockito.when(customerRepository.findById(id)).thenReturn(Mono.just(customerMono));

        Mockito.when(customerRepository.save(customerMono)).thenReturn(Mono.just(customerMono));

        assertDoesNotThrow(() -> customerServiceImpl.update(customerMono, id)
                .subscribe(response -> {
                    assertEquals(customerMono.getLastName(), response.getLastName());
                }));

    }

    @Test
    void findAll() {
        Customer customerMono = Customer.builder()
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Mockito.when(customerRepository.findAll()).thenReturn(Flux.just(customerMono));

        assertDoesNotThrow(() -> customerServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(customerMono.getLastName(), response.getLastName());
                }));
    }

    @Test
    void FindById() {

        Customer customer = new Customer();
        customer.setId("unhb2342342342");
        customer.setFirstName("Juan");
        customer.setLastName("fernandez");
        customer.setDocNumber("23424234234");
        customer.setTypeCustomer(1);
        customer.setDescTypeCustomer("personal");

        Mockito.when(customerRepository.findById("unhb2342342342")).thenReturn(Mono.just(customer));

        assertDoesNotThrow(() -> customerServiceImpl.findById(customer.getId())
                .subscribe(response -> {
                    assertEquals(customer.getLastName(), response.getLastName());
                }));

    }

    @Test
    void Delete() {

        Customer customerMono = Customer.builder()
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        String id = "unhb2342342342";

        Mockito.when(customerRepository.findById("unhb2342342342")).thenReturn(Mono.just(customerMono));
        Mockito.when(customerRepository.delete(customerMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> customerServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Customer(), response);
                }));

    }
}