package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void save(Product product);

    List<Product> findActive();

    Optional<Product> findActiveById(String id);
}
