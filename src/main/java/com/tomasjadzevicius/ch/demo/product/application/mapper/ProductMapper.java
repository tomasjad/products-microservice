package com.tomasjadzevicius.ch.demo.product.application.mapper;

import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    com.tomasjadzevicius.ch.demo.product.model.Product toDTO(Product product);

}
