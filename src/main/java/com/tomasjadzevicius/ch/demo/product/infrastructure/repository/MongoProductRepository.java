package com.tomasjadzevicius.ch.demo.product.infrastructure.repository;

import com.tomasjadzevicius.ch.demo.product.domain.ProductRepository;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
class MongoProductRepository implements ProductRepository {

    private final SpringDataMongoProductRepository repository;

    @Override
    public void save(Product product) {
        repository.save(product);
    }

    @Override
    public List<Product> findActive() {
        return repository.findAByActiveTrue();
    }

    @Override
    public Optional<Product> findActiveById(String id) {
        return repository.findByIdAndActiveTrue(id);
    }
}
