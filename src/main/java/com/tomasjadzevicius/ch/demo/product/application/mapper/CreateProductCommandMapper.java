package com.tomasjadzevicius.ch.demo.product.application.mapper;

import com.tomasjadzevicius.ch.demo.product.domain.model.CreateProductCommand;
import com.tomasjadzevicius.ch.demo.product.model.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateProductCommandMapper {

    CreateProductCommandMapper INSTANCE = Mappers.getMapper(CreateProductCommandMapper.class);

    CreateProductCommand fromProductRequest(ProductRequest request);
}
