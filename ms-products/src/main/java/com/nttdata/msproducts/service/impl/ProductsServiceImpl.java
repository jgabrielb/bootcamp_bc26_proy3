package com.nttdata.msproducts.service.impl;

import com.nttdata.msproducts.model.Products;
import com.nttdata.msproducts.repository.ProductsRepository;
import com.nttdata.msproducts.service.ProductsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

    private static Logger logger = LogManager.getLogger(ProductsServiceImpl.class);

    @Autowired
    ProductsRepository repository;

    @Override
    public Flux<Products> findAll() {
        logger.info("executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Products> save(Products c) {
        logger.info("executing save method");
        return repository.save(c);
    }

    @Override
    public Mono<Products> findById(String id) {
        logger.info("executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Products> update(Products c, String id) {
        logger.info("executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setIndProduct(c.getIndProduct());
                    x.setDescIndProduct(c.getDescIndProduct());
                    x.setTypeProduct(c.getTypeProduct());
                    x.setDescTypeProduct(c.getDescTypeProduct());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Products> delete(String id) {
        logger.info("executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }
}
