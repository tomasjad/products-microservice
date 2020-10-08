package com.tomasjadzevicius.ch.demo.product.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class Product {

    @Id
    private String id;
    @Version
    private long version;
    private String name;
    private BigDecimal price;
    private LocalDate created;
    private boolean active;

    public static Product newProduct(CreateProductCommand command, Clock clock) {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .name(command.getName())
                .price(command.getPrice())
                .created(LocalDate.now(clock))
                .active(true)
                .build();
    }

    public void update(UpdateProductCommand command) {
        this.name = command.getName();
        this.price = command.getPrice();
    }

    public void deactivate() {
        active = false;
    }
}
