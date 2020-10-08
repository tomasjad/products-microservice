package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetProductsInteractor {

    private final ProductRepository repository;

    public List<Product> getProducts() {
        return repository.findActive();
    }
}
