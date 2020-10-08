package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ValidationException;
import com.tomasjadzevicius.ch.demo.product.domain.model.CreateProductCommand;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreateProductInteractorTest {

    private ProductRepository repository;
    private CreateProductInteractor underTest;
    private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2020-10-01T10:00:00Z"), UTC);
        repository = mock(ProductRepository.class);
        underTest = new CreateProductInteractor(repository, clock);
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
    }

    @Test
    void shouldNotAllowBlankName() {
        CreateProductCommand command = CreateProductCommand.builder()
                .name(" ")
                .price(BigDecimal.TEN)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.createProduct(command));
        assertEquals("name can not be blank", exception.getMessage());
    }


    @Test
    void shouldNotAllowNullPrice() {
        CreateProductCommand command = CreateProductCommand.builder()
                .name("productA")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.createProduct(command));
        assertEquals("price can not be null", exception.getMessage());
    }

    @Test
    void shouldNotAllowNegativePrice() {
        CreateProductCommand command = CreateProductCommand.builder()
                .name("productA")
                .price(BigDecimal.valueOf(-25.65D))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.createProduct(command));
        assertEquals("price can not be negative", exception.getMessage());
    }

    @Test
    void shouldCreateNewProduct() {
        CreateProductCommand command = CreateProductCommand.builder()
                .name("productA")
                .price(BigDecimal.valueOf(25.65D))
                .build();

        underTest.createProduct(command);

        verify(repository).save(productArgumentCaptor.capture());
        Product savedProduct = productArgumentCaptor.getValue();

        assertEquals("productA", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(25.65D), savedProduct.getPrice());
        assertTrue(savedProduct.isActive());
        assertNotNull(savedProduct.getId());
        assertEquals(LocalDate.of(2020, 10, 1), savedProduct.getCreated());
    }

}
