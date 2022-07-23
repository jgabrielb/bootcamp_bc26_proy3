package com.nttdata.mswithdrawals.service.impl;

import com.nttdata.mswithdrawals.client.AccountClient;
import com.nttdata.mswithdrawals.model.Account;
import com.nttdata.mswithdrawals.model.Product;
import com.nttdata.mswithdrawals.model.Withdrawals;
import com.nttdata.mswithdrawals.repository.WithdrawalsRepository;
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
class WithdrawalsServiceImplTest {
    @Mock
    private WithdrawalsRepository withdrawalRepository;

    @Mock
    private AccountClient transactionClient;

    @InjectMocks
    private WithdrawalsServiceImpl withdrawalServiceImpl;



    @Test
    void createDepositTest() {
        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Product product = new Product();
        product.setId("83457346534534");
        product.setIndProduct(2);
        product.setDescIndProduct("pasivo");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

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
        account.setProduct(product);


        Mockito.when(transactionClient.getAccountWithDetails(account.getId())).thenReturn(Mono.just(account));
        Mockito.when(withdrawalRepository.save(Mockito.any())).thenReturn(Mono.just(withdrawalMono));

        assertDoesNotThrow(() -> withdrawalServiceImpl.save(withdrawalMono)
                .subscribe(response -> {
                    assertEquals(withdrawalMono.getWithdrawalsAmount(), response.getWithdrawalsAmount());
                }));
    }
    @Test
    void updateDepositTest() {
        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(withdrawalRepository.findById(id)).thenReturn(Mono.just(withdrawalMono));
        Mockito.when(withdrawalRepository.save(withdrawalMono)).thenReturn(Mono.just(withdrawalMono));

        assertDoesNotThrow(() -> withdrawalServiceImpl.update(withdrawalMono,id)
                .subscribe(response -> {
                    assertEquals(withdrawalMono.getWithdrawalsAmount(), response.getWithdrawalsAmount());
                }));
    }

    @Test
    void findAll() {
        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Mockito.when(withdrawalRepository.findAll()).thenReturn(Flux.just(withdrawalMono));

        assertDoesNotThrow(() -> withdrawalServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(withdrawalMono.getWithdrawalsAmount(), response.getWithdrawalsAmount());
                }));
    }

    @Test
    void FindById() {

        Withdrawals withdrawalMono = Withdrawals.builder()
                .id("2346723847262")
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();

        Mockito.when(withdrawalRepository.findById("2346723847262")).thenReturn(Mono.just(withdrawalMono));

        assertDoesNotThrow(() -> withdrawalServiceImpl.findById(withdrawalMono.getId())
                .subscribe(response -> {
                    assertEquals(withdrawalMono.getWithdrawalsAmount(), response.getWithdrawalsAmount());
                }));
    }

    @Test
    void Delete() {

        Withdrawals withdrawalMono = Withdrawals.builder()
                .id(ObjectId.get().toString())
                .withdrawalsDate(LocalDate.now())
                .withdrawalsAmount(BigDecimal.valueOf(2000))
                .currency("PEN")
                .accountId("62ce2098459d957a015af234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(withdrawalRepository.findById("6767668789fds9")).thenReturn(Mono.just(withdrawalMono));
        Mockito.when(withdrawalRepository.delete(withdrawalMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> withdrawalServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Withdrawals(), response);
                }));

    }
}