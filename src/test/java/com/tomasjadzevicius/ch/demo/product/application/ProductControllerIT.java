package com.tomasjadzevicius.ch.demo.product.application;

import com.tomasjadzevicius.ch.demo.product.model.Product;
import com.tomasjadzevicius.ch.demo.product.model.Products;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static com.tomasjadzevicius.ch.demo.product.application.ProductRequestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerIT {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.2.10");
    @Autowired
    private MockMvc mockMvc;
    private ProductControllerInvoker invoker;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getContainerIpAddress);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    @BeforeAll
    static void setUpAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void tearDownAll() {
        mongoDBContainer.stop();
    }

    @BeforeEach
    void setUp() {
        invoker = new ProductControllerInvoker(mockMvc);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void invalidCreateProductRequestShouldResultInBadRequest() throws Exception {
        String response = invoker.saveNewProductAndResultInBadRequestStatus(emptyProductRequest());

        assertTrue(response.contains("Field 'name' is required"));
        assertTrue(response.contains("Field 'price' is required"));
    }

    @Test
    void invalidUpdateProductRequestShouldResultInBadRequest() throws Exception {
        String response = invoker.updateProductAndResultInBadRequestStatus(emptyProductRequest());

        assertTrue(response.contains("Field 'name' is required"));
        assertTrue(response.contains("Field 'price' is required"));
    }

    @Test
    void updateNonExistingOrDeletedProductShouldResultInNotFoundStatus() throws Exception {
        invoker.updateProductAndResultInNotFoundStatus("999999", productRequestB());
    }

    @Test
    void shouldRetrieveAllNotDeletedProducts() throws Exception {
        Product productA = invoker.saveNewProduct(productRequestA());
        Product productB = invoker.saveNewProduct(productRequestB());
        invoker.deleteProduct(productB.getId());

        Products products = invoker.retrieveProducts();

        assertTrue(products.getEmbedded().stream().anyMatch(product -> product.getId().equals(productA.getId())));
        assertFalse(products.getEmbedded().stream().anyMatch(product -> product.getId().equals(productB.getId())));
    }

    @Test
    void shouldUpdateNameAndPriceOfTheProduct() throws Exception {
        Product createdProduct = invoker.saveNewProduct(productRequestA());
        invoker.updateProduct(createdProduct.getId(), productRequestB());

        Product retrievedProductAfterUpdate = invoker.retrieveProduct(createdProduct.getId());
        assertEquals(createdProduct.getId(), retrievedProductAfterUpdate.getId());
        assertEquals("productB", retrievedProductAfterUpdate.getName());
        assertEquals(BigDecimal.TEN, retrievedProductAfterUpdate.getPrice());
    }

    @Test
    void productShouldBeRetrievableAfterCreation() throws Exception {
        Product product = invoker.saveNewProduct(productRequestA());
        Product retrievedProduct = invoker.retrieveProduct(product.getId());

        assertEquals(product.getId(), retrievedProduct.getId());
    }

    @Test
    void fetchForDeletedProductShouldResultInStatusNotFound() throws Exception {
        Product product = invoker.saveNewProduct(productRequestA());
        invoker.deleteProduct(product.getId());
        invoker.fetchProductAndResultInNotFoundStatus(product.getId());
    }

}
