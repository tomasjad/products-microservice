package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ProductNotFoundException;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProductInteractorTest {

    private static final String PRODUCT_ID = "prod-0001";
    private ProductRepository repository;
    private DeleteProductInteractor underTest;
    private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        underTest = new DeleteProductInteractor(repository);
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
    }

    @Test
    void shouldFailWhenProductNotFound() {
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> underTest.deleteProduct(PRODUCT_ID));
        assertEquals("Product prod-0001 not found", exception.getMessage());
    }

    @Test
    void softDeleteShouldOnlyDeactivateProduct() {
        Product product = Product.builder()
                .id(PRODUCT_ID)
                .name("productA")
                .price(BigDecimal.valueOf(25.65D))
                .active(true)
                .created(LocalDate.now())
                .build();
        when(repository.findActiveById(PRODUCT_ID)).thenReturn(Optional.of(product));

        underTest.deleteProduct(PRODUCT_ID);

        verify(repository).save(productArgumentCaptor.capture());

        Product savedProduct = productArgumentCaptor.getValue();
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getCreated(), savedProduct.getCreated());
        assertFalse(savedProduct.isActive());
    }
}
