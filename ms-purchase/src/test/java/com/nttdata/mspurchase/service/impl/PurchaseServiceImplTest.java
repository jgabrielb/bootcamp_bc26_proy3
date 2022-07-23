package com.nttdata.mspurchase.service.impl;

import com.nttdata.mspurchase.client.AccountClient;
import com.nttdata.mspurchase.model.Account;
import com.nttdata.mspurchase.model.Product;
import com.nttdata.mspurchase.model.Purchase;
import com.nttdata.mspurchase.repository.PurchaseRepository;
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
class PurchaseServiceImplTest {
    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private PurchaseServiceImpl purchaseServiceImpl;

    @Test
    void createDepositTest() {
        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();

        Product product = new Product();
        product.setId("83457346534534");
        product.setIndProduct(2);
        product.setDescIndProduct("pasivo");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Account account = new Account();
        account.setId("34242423234");
        account.setCustomerId("62ce61cafc71236753d97075");
        account.setProductId("62ce61b8277f3b117e9238ce");
        account.setAccountNumber("384724239423");
        account.setAccountNumberInt("384724239423");
        account.setMovementLimits(1);
        account.setMovementActually(1);
        account.setCreditLimits(BigDecimal.valueOf(100));
        account.setCreditActually(BigDecimal.valueOf(7500));
        account.setCommission(BigDecimal.valueOf(30));
        account.setMovementDate(LocalDate.parse("2022-10-12"));
        account.setProduct(product);


        Mockito.when(accountClient.getAccountWithDetails(account.getId())).thenReturn(Mono.just(account));
        Mockito.when(purchaseRepository.save(Mockito.any())).thenReturn(Mono.just(purchaseMono));

        assertDoesNotThrow(() -> purchaseServiceImpl.save(purchaseMono)
                .subscribe(response -> {
                    assertEquals(purchaseMono.getPurchaseAmount(), response.getPurchaseAmount());
                }));
    }

    @Test
    void updateDepositTest() {
        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(purchaseRepository.findById(id)).thenReturn(Mono.just(purchaseMono));
        Mockito.when(purchaseRepository.save(purchaseMono)).thenReturn(Mono.just(purchaseMono));

        assertDoesNotThrow(() -> purchaseServiceImpl.update(purchaseMono,id)
                .subscribe(response -> {
                    assertEquals(purchaseMono.getPurchaseAmount(), response.getPurchaseAmount());
                }));
    }

    @Test
    void findAll() {
        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();

        Mockito.when(purchaseRepository.findAll()).thenReturn(Flux.just(purchaseMono));

        assertDoesNotThrow(() -> purchaseServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(purchaseMono.getPurchaseAmount(), response.getPurchaseAmount());
                }));
    }

    @Test
    void FindById() {

        Purchase purchase = new Purchase();
        purchase.setId("2346723847262");
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setPurchaseAmount(BigDecimal.valueOf(100));
        purchase.setDescription("Servicio Claro");
        purchase.setCurrency("PEN");
        purchase.setAccountId("34242423234");

        Mockito.when(purchaseRepository.findById("2346723847262")).thenReturn(Mono.just(purchase));

        assertDoesNotThrow(() -> purchaseServiceImpl.findById(purchase.getId())
                .subscribe(response -> {
                    assertEquals(purchase.getPurchaseAmount(), response.getPurchaseAmount());
                }));
    }

    @Test
    void Delete() {

        Purchase purchaseMono = Purchase.builder()
                .id(ObjectId.get().toString())
                .purchaseDate(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(purchaseRepository.findById("6767668789fds9")).thenReturn(Mono.just(purchaseMono));
        Mockito.when(purchaseRepository.delete(purchaseMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> purchaseServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Purchase(), response);
                }));

    }
}