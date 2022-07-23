package com.nttdata.mssignatories.service;

import com.nttdata.mssignatories.model.Signatories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SignatoriesService {
    Flux<Signatories> findAll();

    Mono<Signatories> save(Signatories c);

    Mono<Signatories> findById(String id);

    Mono<Signatories> update(Signatories c, String id);

    Mono<Signatories> delete(String id);
}
