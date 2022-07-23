package com.nttdata.mspurchase.repository;

import com.nttdata.mspurchase.model.Purchase;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PurchaseRepository extends ReactiveCrudRepository<Purchase, String> {
}
