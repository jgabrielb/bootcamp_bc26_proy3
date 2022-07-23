package com.nttdata.msdeposits.service.impl;

import com.nttdata.msdeposits.client.AccountClient;
import com.nttdata.msdeposits.model.Account;
import com.nttdata.msdeposits.model.Customer;
import com.nttdata.msdeposits.model.Deposits;
import com.nttdata.msdeposits.model.Product;
import com.nttdata.msdeposits.repository.DepositsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class DepositsServiceImplTest {
    @Mock
    private DepositsRepository depositRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private DepositsServiceImpl depositServiceImpl;

    @Test
    void createDepositTest() {
        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Product product = new Product();
        product.setId("83457346534534");
        product.setIndProduct(1);
        product.setDescIndProduct("PASIVOS");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Customer customer = new Customer();
        customer.setId("12buhvg24uhjknv2");
        customer.setFirstName("Juan");
        customer.setLastName("fernandez");
        customer.setDocNumber("98765432");
        customer.setTypeCustomer(1);
        customer.setDescTypeCustomer("personal");

        Account account = new Account();
        account.setId("62ce2098459d957a015af234");
        account.setCustomerId("12buhvg24uhjknv2");
        account.setProductId("83457346534534");
        account.setAccountNumber("9946548947640");
        account.setAccountNumberInt("00799401654894764075");
        account.setMovementLimits(1);
        account.setMovementActually(5);
        account.setCreditLimits(BigDecimal.valueOf(100));
        account.setCreditActually(BigDecimal.valueOf(7500));
        account.setCommission(BigDecimal.valueOf(5));
        account.setMovementDate(LocalDate.parse("2022-10-12"));
        account.setCustomer(customer);
        account.setProduct(product);



        Mockito.when(accountClient.getAccountWithDetails(account.getId())).thenReturn(Mono.just(account));
        Mockito.when(depositRepository.save(Mockito.any())).thenReturn(Mono.just(depositMono));

        assertDoesNotThrow(() -> depositServiceImpl.save(depositMono)
                .subscribe(response -> {
                    assertEquals(depositMono.getDepositAmount(), response.getDepositAmount());
                }));
    }

    @Test
    void updateDepositTest() {
        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("234234jnjk2344")
                .build();
        String id = "6767668789fds9";

        Deposits deposit = new Deposits();
        BeanUtils.copyProperties(depositMono,deposit);

        Mockito.when(depositRepository.findById(id)).thenReturn(Mono.just(deposit));
        Mockito.when(depositRepository.save(deposit)).thenReturn(Mono.just(deposit));

        assertDoesNotThrow(() -> depositServiceImpl.update(depositMono,id)
                .subscribe(response -> {
                    assertEquals(depositMono.getDepositAmount(), response.getDepositAmount());
                }));
    }

    @Test
    void findAll() {
        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("234234jnjk2344")
                .build();

        Mockito.when(depositRepository.findAll()).thenReturn(Flux.just(depositMono));

        assertDoesNotThrow(() -> depositServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(depositMono.getDepositAmount(), response.getDepositAmount());
                }));
    }

    @Test
    void FindById() {

        Deposits deposit = new Deposits();
        deposit.setId("62ce61b8277f3b117e9238ce");
        deposit.setDepositDate(LocalDate.now());
        deposit.setDepositAmount(BigDecimal.valueOf(200));
        deposit.setCurrency("PEN");
        deposit.setAccountId("unhb2342342342");

        Mockito.when(depositRepository.findById("62ce61b8277f3b117e9238ce")).thenReturn(Mono.just(deposit));

        assertDoesNotThrow(() -> depositServiceImpl.findById(deposit.getId())
                .subscribe(response -> {
                    assertEquals(deposit.getDepositAmount(), response.getDepositAmount());
                }));
    }

    @Test
    void Delete() {

        Deposits depositMono = Deposits.builder()
                .id(ObjectId.get().toString())
                .depositDate(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .currency("PEN")
                .accountId("234234jnjk2344")
                .build();

        String id = "unhb2342342342";

        Mockito.when(depositRepository.findById("unhb2342342342")).thenReturn(Mono.just(depositMono));
        Mockito.when(depositRepository.delete(depositMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> depositServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Deposits(), response);
                }));

    }
}