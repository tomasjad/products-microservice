package com.tomasjadzevicius.ch.demo.product.domain.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
