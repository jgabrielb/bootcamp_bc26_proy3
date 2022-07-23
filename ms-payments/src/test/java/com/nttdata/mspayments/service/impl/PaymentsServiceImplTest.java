package com.nttdata.mspayments.service.impl;

import com.nttdata.mspayments.client.AccountClient;
import com.nttdata.mspayments.model.Account;
import com.nttdata.mspayments.model.Payments;
import com.nttdata.mspayments.model.Product;
import com.nttdata.mspayments.repository.PaymentsRepository;
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
class PaymentsServiceImplTest {
    @Mock
    private PaymentsRepository paymentRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private PaymentsServiceImpl paymentServiceImpl;

    @Test
    void createPaymentTest() {
        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
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


        Mockito.when(accountClient.getAccountWithDetails(account.getId())).thenReturn(Mono.just(account));
        Mockito.when(paymentRepository.save(Mockito.any())).thenReturn(Mono.just(paymentMono));

        assertDoesNotThrow(() -> paymentServiceImpl.save(paymentMono)
                .subscribe(response -> {
                    assertEquals(paymentMono.getPaymentAmount(), response.getPaymentAmount());
                }));
    }

    @Test
    void updatePaymentTest() {
        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();

        String id = "6767668789fds9";

        Mockito.when(paymentRepository.findById(id)).thenReturn(Mono.just(paymentMono));
        Mockito.when(paymentRepository.save(paymentMono)).thenReturn(Mono.just(paymentMono));

        assertDoesNotThrow(() -> paymentServiceImpl.update(paymentMono,id)
                .subscribe(response -> {
                    assertEquals(paymentMono.getPaymentAmount(), response.getPaymentAmount());
                }));
    }

    @Test
    void findAll() {
        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();

        Mockito.when(paymentRepository.findAll()).thenReturn(Flux.just(paymentMono));

        assertDoesNotThrow(() -> paymentServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(paymentMono.getPaymentAmount(), response.getPaymentAmount());
                }));
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

        Mockito.when(paymentRepository.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(payment));

        assertDoesNotThrow(() -> paymentServiceImpl.findById(payment.getId())
                .subscribe(response -> {
                    assertEquals(payment.getPaymentAmount(), response.getPaymentAmount());
                }));
    }

    @Test
    void Delete() {

        Payments paymentMono = Payments.builder()
                .id(ObjectId.get().toString())
                .paymentDate(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("Servicio Claro")
                .currency("PEN")
                .accountId("34242423234")
                .build();
        String id = "6767668789fds9";

        Mockito.when(paymentRepository.findById("6767668789fds9")).thenReturn(Mono.just(paymentMono));
        Mockito.when(paymentRepository.delete(paymentMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> paymentServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Payments(), response);
                }));

    }
}