package com.nttdata.mssignatories.repository;

import com.nttdata.mssignatories.model.Signatories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatoriesRepository extends ReactiveCrudRepository<Signatories, String> {
}
