package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ProductNotFoundException;
import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ValidationException;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import com.tomasjadzevicius.ch.demo.product.domain.model.UpdateProductCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductInteractorTest {

    private static final String PRODUCT_ID = "prod-0001";
    private ProductRepository repository;
    private UpdateProductInteractor underTest;
    private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        underTest = new UpdateProductInteractor(repository);
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
    }

    @Test
    void shouldNotAllowBlankName() {
        UpdateProductCommand command = UpdateProductCommand.builder()
                .name(" ")
                .price(BigDecimal.TEN)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.updateProduct(PRODUCT_ID, command));
        assertEquals("name can not be blank", exception.getMessage());
    }

    @Test
    void shouldNotAllowNullPrice() {
        UpdateProductCommand command = UpdateProductCommand.builder()
                .name("productA")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.updateProduct(PRODUCT_ID, command));
        assertEquals("price can not be null", exception.getMessage());
    }

    @Test
    void shouldNotAllowNegativePrice() {
        UpdateProductCommand command = UpdateProductCommand.builder()
                .name("productA")
                .price(BigDecimal.valueOf(-25.65D))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.updateProduct(PRODUCT_ID, command));
        assertEquals("price can not be negative", exception.getMessage());
    }

    @Test
    void shouldFailWhenProductNotFound() {
        UpdateProductCommand command = UpdateProductCommand.builder()
                .name("productA")
                .price(BigDecimal.valueOf(25.65D))
                .build();

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> underTest.updateProduct(PRODUCT_ID, command));
        assertEquals("Product prod-0001 not found", exception.getMessage());
    }

    @Test
    void shouldUpdateNameAndPriceOfTheProduct() {
        UpdateProductCommand command = UpdateProductCommand.builder()
                .name("productB")
                .price(BigDecimal.valueOf(31.22D))
                .build();
        Product product = Product.builder()
                .id(PRODUCT_ID)
                .name("productA")
                .price(BigDecimal.valueOf(25.65D))
                .active(true)
                .created(LocalDate.now())
                .build();

        when(repository.findActiveById(PRODUCT_ID)).thenReturn(Optional.of(product));

        underTest.updateProduct(PRODUCT_ID, command);

        verify(repository).save(productArgumentCaptor.capture());
        Product savedProduct = productArgumentCaptor.getValue();
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals("productB", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(31.22D), savedProduct.getPrice());
        assertEquals(product.getCreated(), savedProduct.getCreated());
        assertTrue(savedProduct.isActive());
    }
}
