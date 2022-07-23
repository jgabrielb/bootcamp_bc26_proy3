package com.nttdata.mswithdrawals.repository;

import com.nttdata.mswithdrawals.model.Withdrawals;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalsRepository extends ReactiveCrudRepository<Withdrawals, String> {
}
