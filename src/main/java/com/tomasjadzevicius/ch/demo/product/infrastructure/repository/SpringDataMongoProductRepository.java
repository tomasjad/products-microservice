package com.tomasjadzevicius.ch.demo.product.infrastructure.repository;

import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataMongoProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByIdAndActiveTrue(String id);

    List<Product> findAByActiveTrue();
}
