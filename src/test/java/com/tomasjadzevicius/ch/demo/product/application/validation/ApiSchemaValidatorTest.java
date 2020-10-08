package com.tomasjadzevicius.ch.demo.product.application.validation;

import com.tomasjadzevicius.ch.demo.product.model.ProductRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiSchemaValidatorTest {

    private final ApiSchemaValidator underTest = new ApiSchemaValidator();

    @Test
    void productRequestWithoutNameShouldFail() {
        ProductRequest request = new ProductRequest();
        request.setPrice(BigDecimal.TEN);

        SchemaValidationException exception = assertThrows(SchemaValidationException.class, () -> underTest.validate(request));
        assertTrue(exception.getMessage().contains("Field 'name' is required."));
    }

    @Test
    void productRequestWithoutPriceShouldFail() {
        ProductRequest request = new ProductRequest();
        request.setName("productA");

        SchemaValidationException exception = assertThrows(SchemaValidationException.class, () -> underTest.validate(request));
        assertTrue(exception.getMessage().contains("Field 'price' is required."));
    }

    @Test
    void productRequestWithNegativePriceShouldFail() {
        ProductRequest request = new ProductRequest();
        request.setName("productA");
        request.setPrice(BigDecimal.valueOf(-25.65D));

        SchemaValidationException exception = assertThrows(SchemaValidationException.class, () -> underTest.validate(request));
        assertTrue(exception.getMessage().contains("price: Minimum is '0', found '-25.65'."));
    }
}
