package com.tomasjadzevicius.ch.demo.product.application.mapper;

import com.tomasjadzevicius.ch.demo.product.domain.model.UpdateProductCommand;
import com.tomasjadzevicius.ch.demo.product.model.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateProductCommandMapper {

    UpdateProductCommandMapper INSTANCE = Mappers.getMapper(UpdateProductCommandMapper.class);

    UpdateProductCommand fromProductRequest(ProductRequest request);
}
