package com.nttdata.msproducts.repository;

import com.nttdata.msproducts.model.Products;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends ReactiveCrudRepository<Products, String> {
}
