package com.nttdata.msdeposits.repository;

import com.nttdata.msdeposits.model.Deposits;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositsRepository extends ReactiveCrudRepository<Deposits, String> {
}
