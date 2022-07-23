package com.nttdata.mspayments.repository;

import com.nttdata.mspayments.model.Payments;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends ReactiveCrudRepository<Payments, String> {
}
