package com.tomasjadzevicius.ch.demo.product.domain.model;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ValidationException;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateProductCommand {

    private String name;
    private BigDecimal price;

    public void validate() {
        if (StringUtils.isBlank(name)) {
            throw new ValidationException("name can not be blank");
        }
        if (price == null) {
            throw new ValidationException("price can not be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("price can not be negative");
        }
    }
}
