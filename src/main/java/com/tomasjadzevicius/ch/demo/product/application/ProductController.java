package com.tomasjadzevicius.ch.demo.product.application;

import com.tomasjadzevicius.ch.demo.product.api.ProductsApi;
import com.tomasjadzevicius.ch.demo.product.application.mapper.CreateProductCommandMapper;
import com.tomasjadzevicius.ch.demo.product.application.mapper.ProductMapper;
import com.tomasjadzevicius.ch.demo.product.application.mapper.UpdateProductCommandMapper;
import com.tomasjadzevicius.ch.demo.product.application.validation.ApiSchemaValidator;
import com.tomasjadzevicius.ch.demo.product.domain.*;
import com.tomasjadzevicius.ch.demo.product.model.*;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@AllArgsConstructor
public class ProductController implements ProductsApi {

    private final ApiSchemaValidator schemaValidator;
    private final CreateProductInteractor createProductInteractor;
    private final UpdateProductInteractor updateProductInteractor;
    private final GetProductInteractor getProductInteractor;
    private final GetProductsInteractor getProductsInteractor;
    private final DeleteProductInteractor deleteProductInteractor;


    @Override
    public ResponseEntity<Product> createProduct(ProductRequest productRequest) {
        schemaValidator.validate(productRequest);
        var createProductCommand = CreateProductCommandMapper.INSTANCE.fromProductRequest(productRequest);
        var product = createProductInteractor.createProduct(createProductCommand);
        var productDTO = mapAndEnrichProduct(product);

        return status(CREATED).body(productDTO);
    }

    @Override
    public ResponseEntity<Product> getProduct(String productId) {
        var product = getProductInteractor.getProduct(productId);
        var productDTO = mapAndEnrichProduct(product);

        return ResponseEntity.ok(productDTO);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String productId) {
        deleteProductInteractor.deleteProduct(productId);
        return status(OK).build();
    }

    @Override
    public ResponseEntity<Product> updateProduct(String productId, ProductRequest productRequest) {
        schemaValidator.validate(productRequest);
        var editProductCommand = UpdateProductCommandMapper.INSTANCE.fromProductRequest(productRequest);
        var product = updateProductInteractor.updateProduct(productId, editProductCommand);
        var productDTO = mapAndEnrichProduct(product);

        return status(OK).body(productDTO);
    }

    @Override
    public ResponseEntity<Products> getAllProducts() {
        List<Product> products = getProductsInteractor.getProducts().stream().map(this::mapAndEnrichProduct).collect(Collectors.toList());
        Products productsDTO = new Products();
        productsDTO.setEmbedded(products);

        return ResponseEntity.ok(productsDTO);
    }

    private Product mapAndEnrichProduct(com.tomasjadzevicius.ch.demo.product.domain.model.Product product) {
        ProductLinks productLinks = createProductLinks(product);
        Product productDTO = ProductMapper.INSTANCE.toDTO(product);
        productDTO.setLinks(productLinks);

        return productDTO;
    }

    private ProductLinks createProductLinks(com.tomasjadzevicius.ch.demo.product.domain.model.Product product) {
        Link selfLink = linkTo(methodOn(ProductController.class).getProduct(product.getId())).withSelfRel();
        Link updateLink = linkTo(methodOn(ProductController.class).updateProduct(product.getId(), null)).withSelfRel();
        Link deleteLink = linkTo(methodOn(ProductController.class).deleteProduct(product.getId())).withSelfRel();

        ProductLinks productLinks = new ProductLinks();
        productLinks.setSelf(createProductLink(selfLink));
        productLinks.setUpdate(createProductLink(updateLink));
        productLinks.setDelete(createProductLink(deleteLink));

        return productLinks;
    }

    private ProductLink createProductLink(Link link) {
        ProductLink productLink = new ProductLink();
        productLink.setHref(link.getHref());

        return productLink;
    }
}
