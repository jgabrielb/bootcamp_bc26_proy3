package com.nttdata.mssignatories.service.impl;

import com.nttdata.mssignatories.client.AccountClient;
import com.nttdata.mssignatories.client.CustomerClient;
import com.nttdata.mssignatories.client.ProductClient;
import com.nttdata.mssignatories.model.Account;
import com.nttdata.mssignatories.model.Customer;
import com.nttdata.mssignatories.model.Product;
import com.nttdata.mssignatories.model.Signatories;
import com.nttdata.mssignatories.repository.SignatoriesRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class SignatoriesServiceImplTest {
    @Mock
    private SignatoriesRepository signatoryRepository;

    @Mock
    private AccountClient accountClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private SignatoriesServiceImpl signatoryServiceImpl;

    @Test
    void createSignatoriesTest() {
        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("62ce2098459d957a015af234")
                .build();

        Product product = new Product();
        product.setId("83457346534534");
        product.setIndProduct(2);
        product.setDescIndProduct("pasivo");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Customer customer = new Customer();
        customer.setId("unhb2342342342");
        customer.setFirstName("Juan");
        customer.setLastName("fernandez");
        customer.setDocNumber("98765432");
        customer.setTypeCustomer(1);
        customer.setDescTypeCustomer("personal");

        Account account = new Account();
        account.setId("62ce2098459d957a015af234");
        account.setCustomerId("7890729347");
        account.setProductId("8974593463");
        account.setAccountNumber("384724239423");
        account.setAccountNumberInt("384724239423");
        account.setMovementLimits(1);
        account.setMovementActually(1);
        account.setCreditLimits(BigDecimal.valueOf(100));
        account.setCreditActually(BigDecimal.valueOf(30));
        account.setCommission(BigDecimal.valueOf(30));
        account.setMovementDate(LocalDate.parse("2022-10-12"));
        account.setCustomer(customer);
        account.setProduct(product);

        Mockito.when(accountClient.getAccountWithDetails(account.getId())).thenReturn(Mono.just(account));
        Mockito.when(productClient.getProduct(product.getId())).thenReturn(Mono.just(product));
        Mockito.when(customerClient.getCustomer(customer.getId())).thenReturn(Mono.just(customer));
        Mockito.when(signatoryRepository.save(Mockito.any())).thenReturn(Mono.just(signatoryMono));

        assertDoesNotThrow(() -> signatoryServiceImpl.save(signatoryMono)
                .subscribe(response -> {
                    assertEquals(signatoryMono.getDocNumber(), response.getDocNumber());
                }));
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

        String id = "6767668789fds9";

        Mockito.when(signatoryRepository.findById(id)).thenReturn(Mono.just(signatoryMono));
        Mockito.when(signatoryRepository.save(signatoryMono)).thenReturn(Mono.just(signatoryMono));

        assertDoesNotThrow(() -> signatoryServiceImpl.update(signatoryMono,id)
                .subscribe(response -> {
                    assertEquals(signatoryMono.getDocNumber(), response.getDocNumber());
                }));
    }

    @Test
    void findAll() {

        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("34242423234")
                .build();

        Mockito.when(signatoryRepository.findAll()).thenReturn(Flux.just(signatoryMono));

        assertDoesNotThrow(() -> signatoryServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(signatoryMono.getDocNumber(), response.getDocNumber());
                }));
    }


    @Test
    void FindById() {

        Signatories signatories = new Signatories();
        signatories.setId("2346723847262");
        signatories.setFirstName("Juan");
        signatories.setLastName("fernandez");
        signatories.setDocNumber("98765432");
        signatories.setAccountId("34242423234");

        Mockito.when(signatoryRepository.findById("2346723847262")).thenReturn(Mono.just(signatories));

        assertDoesNotThrow(() -> signatoryServiceImpl.findById(signatories.getId())
                .subscribe(response -> {
                    assertEquals(signatories.getDocNumber(), response.getDocNumber());
                }));
    }

    @Test
    void Delete() {

        Signatories signatoryMono = Signatories.builder()
                .id(ObjectId.get().toString())
                .firstName("Juan")
                .lastName("fernandez")
                .docNumber("98765432")
                .accountId("34242423234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(signatoryRepository.findById("6767668789fds9")).thenReturn(Mono.just(signatoryMono));
        Mockito.when(signatoryRepository.delete(signatoryMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> signatoryServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Signatories(), response);
                }));

    }
}