package com.tomasjadzevicius.ch.demo.product.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tomasjadzevicius.ch.demo.product.model.Product;
import com.tomasjadzevicius.ch.demo.product.model.ProductRequest;
import com.tomasjadzevicius.ch.demo.product.model.Products;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerInvoker {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    private MockMvc mockMvc;

    ProductControllerInvoker(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    String saveNewProductAndResultInBadRequestStatus(ProductRequest productRequest) throws Exception {
        MvcResult result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    String updateProductAndResultInBadRequestStatus(ProductRequest productRequest) throws Exception {
        MvcResult result = mockMvc.perform(put("/products/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    Product saveNewProduct(ProductRequest productRequest) throws Exception {
        MvcResult result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return mapper.readValue(response, Product.class);
    }

    Product updateProduct(String productId, ProductRequest productRequest) throws Exception {
        MvcResult result = mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return mapper.readValue(response, Product.class);
    }

    Product retrieveProduct(String productId) throws Exception {
        MvcResult result = mockMvc.perform(get("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return mapper.readValue(response, Product.class);
    }

    void deleteProduct(String productId) throws Exception {
        mockMvc.perform(delete("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    Products retrieveProducts() throws Exception {
        MvcResult result = mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return mapper.readValue(response, Products.class);
    }

    void fetchProductAndResultInNotFoundStatus(String productId) throws Exception {
        mockMvc.perform(get("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    void updateProductAndResultInNotFoundStatus(String productId, ProductRequest productRequest) throws Exception {
        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());
    }
}
