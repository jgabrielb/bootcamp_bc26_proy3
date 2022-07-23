package com.nttdata.msproducts.service.impl;

import com.nttdata.msproducts.model.Products;
import com.nttdata.msproducts.repository.ProductsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ProductsServiceImplTest {
    @Mock
    private ProductsRepository productRepository;

    @InjectMocks
    private ProductsServiceImpl productServiceImpl;

    @Test
    void createDepositTest() {
        Products productMono = Products.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(Mono.just(productMono));

        assertDoesNotThrow(() -> productServiceImpl.save(productMono)
                .subscribe(response -> {
                    assertEquals(productMono.getIndProduct(), response.getIndProduct());
                }));
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
        Mockito.when(productRepository.findById(id)).thenReturn(Mono.just(productMono));
        Mockito.when(productRepository.save(productMono)).thenReturn(Mono.just(productMono));


        assertDoesNotThrow(() -> productServiceImpl.update(productMono, id)
                .subscribe(response -> {
                    assertEquals(productMono.getIndProduct(), response.getIndProduct());
                }));
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

        Mockito.when(productRepository.findAll()).thenReturn(Flux.just(productMono));

        assertDoesNotThrow(() -> productServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(productMono.getIndProduct(), response.getIndProduct());
                }));
    }
    @Test
    void FindById() {

        Products product = new Products();
        product.setId("12buhvg24uhjknv2");
        product.setIndProduct(1);
        product.setDescIndProduct("cuenta bancaria");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Mockito.when(productRepository.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(product));

        assertDoesNotThrow(() -> productServiceImpl.findById(product.getId())
                .subscribe(response -> {
                    assertEquals(product.getIndProduct(), response.getIndProduct());
                }));
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
        String id = "unhb2342342342";

        Mockito.when(productRepository.findById("unhb2342342342")).thenReturn(Mono.just(productMono));
        Mockito.when(productRepository.delete(productMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> productServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Products(), response);
                }));

    }
}