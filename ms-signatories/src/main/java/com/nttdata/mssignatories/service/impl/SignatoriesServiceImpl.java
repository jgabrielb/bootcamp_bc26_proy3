package com.nttdata.mssignatories.service.impl;

import com.nttdata.mssignatories.client.AccountClient;
import com.nttdata.mssignatories.model.Signatories;
import com.nttdata.mssignatories.repository.SignatoriesRepository;
import com.nttdata.mssignatories.service.SignatoriesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SignatoriesServiceImpl implements SignatoriesService {

    private static Logger logger = LogManager.getLogger(SignatoriesServiceImpl.class);

    @Autowired
    SignatoriesRepository repository;

    @Autowired
    AccountClient accountClient;

    @Override
    public Flux<Signatories> findAll() {
        logger.info("Executing findAll method");
        return repository.findAll();
    }

    @Override
    public Mono<Signatories> save(Signatories c) {
        logger.info("Executing save method");
        return accountClient.getAccountWithDetails(c.getAccountId())
                .filter( x -> x.getProduct().getIndProduct() == 1)
                .filter(z -> z.getCustomer().getTypeCustomer() == 2)
                .hasElement()
                .flatMap( y -> {
                    if(y){
                        return repository.save(c);
                    }else{
                        return Mono.error(new RuntimeException("La cuenta ingresada no es una cuenta bancaria empresarial"));
                    }
                });
    }

    @Override
    public Mono<Signatories> findById(String id) {
        logger.info("Executing findById method");
        return repository.findById(id);
    }

    @Override
    public Mono<Signatories> update(Signatories c, String id) {
        logger.info("Executing update method");
        return repository.findById(id)
                .flatMap( x -> {
                    x.setFirstName(c.getFirstName());
                    x.setLastName(c.getLastName());
                    x.setDocNumber(c.getDocNumber());
                    x.setAccountId(c.getAccountId());
                    return repository.save(x);
                });
    }

    @Override
    public Mono<Signatories> delete(String id) {
        logger.info("Executing delete method");
        return repository.findById(id)
                .flatMap( x -> repository.delete(x)
                        .then(Mono.just(x)));
    }
}
