package com.tomasjadzevicius.ch.demo.product.application.validation;

public class SchemaValidationException extends RuntimeException {

    public SchemaValidationException(String message) {
        super(message);
    }
}
