package com.tomasjadzevicius.ch.demo.product.application.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.schema.validator.v3.SchemaValidator;
import org.springframework.stereotype.Component;

import java.net.URL;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Component
public class ApiSchemaValidator {

    private static final OpenApi3 apiSpec;
    private static final ObjectMapper mapper;

    static {
        try {
            URL apiFile = Thread.currentThread().getContextClassLoader().getResource("products-api.yaml");
            apiSpec = new OpenApi3Parser().parse(apiFile, null, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
    }

    public void validate(Object objectToValidate) {
        String schemaName = objectToValidate.getClass().getSimpleName();
        try {
            SchemaValidator schemaValidator = new SchemaValidator(null, apiSpec.getComponents().getSchema(schemaName).toNode());
            JsonNode node = mapper.convertValue(objectToValidate, JsonNode.class);
            schemaValidator.validate(node);
        } catch (ValidationException e) {
            throw new SchemaValidationException(e.results().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
