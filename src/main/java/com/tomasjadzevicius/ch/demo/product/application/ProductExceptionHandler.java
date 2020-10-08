package com.tomasjadzevicius.ch.demo.product.application;

import com.tomasjadzevicius.ch.demo.product.application.validation.SchemaValidationException;
import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
class ProductExceptionHandler {

    @ExceptionHandler(value = ProductNotFoundException.class)
    ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = SchemaValidationException.class)
    ResponseEntity<String> handleSchemaValidationException(SchemaValidationException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
