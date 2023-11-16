package com.imeasystems.orderservice.customer.controller;

import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerResponse;
import com.imeasystems.orderservice.customer.dto.UpdateCustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String CUSTOMER_URL = "/api/v1/customers";

    @Test
    @Sql(value = "classpath:customer/controller/cleanUp.sql", executionPhase = AFTER_TEST_METHOD)
    void createCustomerSuccessTest() {
        CreateCustomerDto createCustomerDto =
                new CreateCustomerDto("Anna", null, "anna@email.com", "+94555555");

        ResponseEntity<CustomerDto> createResponse =
                restTemplate.postForEntity(CUSTOMER_URL, createCustomerDto, CustomerDto.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertTrue(createResponse.hasBody());
        assertNotNull(createResponse.getBody().getId());
        assertThat(createResponse.getBody()).isNotNull();
        assertEquals("Anna", createResponse.getBody().getName());
    }

    @Test
    void createCustomerFailTest_emptyName() {
        CreateCustomerDto createCustomerDto =
                new CreateCustomerDto(null, null, "anna@email.com", "+94555555");

        ResponseEntity<Map> createResponse =
                restTemplate.postForEntity(CUSTOMER_URL, createCustomerDto, Map.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, createResponse.getStatusCode());
        assertTrue(createResponse.hasBody());
        assertTrue(createResponse.getBody().containsKey("name"));
        assertEquals("must not be null", createResponse.getBody().get("name"));
    }

    @Test
    void createCustomerFailTest_invalidEmail() {
        CreateCustomerDto createCustomerDto =
                new CreateCustomerDto("Anna", "Smith", "invalid-email", "+94555555");

        ResponseEntity<Map> createResponse =
                restTemplate.postForEntity(CUSTOMER_URL, createCustomerDto, Map.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, createResponse.getStatusCode());
        assertTrue(createResponse.hasBody());
        assertTrue(createResponse.getBody().containsKey("email"));
        assertEquals("must be a well-formed email address", createResponse.getBody().get("email"));
    }

    @Test
    void createCustomerFailTest_invalidPhone() {
        CreateCustomerDto createCustomerDto =
                new CreateCustomerDto("Anna", "Smith", "anna@email.com", "+94555");

        ResponseEntity<Map> createResponse =
                restTemplate.postForEntity(CUSTOMER_URL, createCustomerDto, Map.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, createResponse.getStatusCode());
        assertTrue(createResponse.hasBody());
        assertTrue(createResponse.getBody().containsKey("phoneNumber"));
        assertEquals("must match \"^\\+{0,1}[0-9]{8,}$\"", createResponse.getBody().get("phoneNumber"));
    }

    @Test
    @Sql(value = "classpath:customer/controller/getAllCustomersSuccessTest/getAllCustomersSuccessTest.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:customer/controller/cleanUp.sql", executionPhase = AFTER_TEST_METHOD)
    void getAllCustomersSuccessTest() {
        ResponseEntity<CustomerResponse> response =
                restTemplate.getForEntity(CUSTOMER_URL, CustomerResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        CustomerResponse body = response.getBody();
        assertEquals(2, body.getTotalItems());
        List<CustomerDto> customers = body.getCustomers();
        if (customers.get(0).getId().equals(1001L)) {  //just to be sure
            assertEquals("Robert", customers.get(0).getName());
            assertEquals("Williams", customers.get(0).getSurname());
            assertEquals("robert@email.com", customers.get(0).getEmail());
            assertEquals("0091234567", customers.get(0).getPhoneNumber());
        }
        if (customers.get(1).getId().equals(1002L)) {  //just to be sure
            assertEquals("Emma", customers.get(1).getName());
            assertEquals("Brown", customers.get(1).getSurname());
            assertEquals("emma@email.com", customers.get(1).getEmail());
            assertEquals("+374555555", customers.get(1).getPhoneNumber());
        }
    }

    @Test
    @Sql(value = "classpath:customer/controller/getCustomerSuccessTest/getCustomerSuccessTest.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:customer/controller/cleanUp.sql", executionPhase = AFTER_TEST_METHOD)
    void getCustomerSuccessTest() {
        ResponseEntity<CustomerDto> response =
                restTemplate.getForEntity(CUSTOMER_URL + "/1005", CustomerDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        CustomerDto customer = response.getBody();
        assertEquals(1005L, customer.getId());
        assertEquals("David", customer.getName());
        assertEquals("Miller", customer.getSurname());
        assertEquals("david@email.com", customer.getEmail());
        assertEquals("+374895645", customer.getPhoneNumber());
    }

    @Test
    void getCustomerFailTest() {
        ResponseEntity<Map> response =
                restTemplate.getForEntity(CUSTOMER_URL + "/1005", Map.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Customer with id 1005 not found", response.getBody().get("message"));
    }

    @Test
    @Sql(value = "classpath:customer/controller/updateCustomerSuccessTest/updateCustomerSuccessTest.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:customer/controller/cleanUp.sql", executionPhase = AFTER_TEST_METHOD)
    void updateCustomerSuccessTest() {
        UpdateCustomerDto updateCustomerDto =
                new UpdateCustomerDto(null,"New-Surname", null, null);

         restTemplate.put(CUSTOMER_URL + "/1001", updateCustomerDto);

         // check call
        ResponseEntity<CustomerDto> response =
                restTemplate.getForEntity(CUSTOMER_URL + "/1001", CustomerDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        CustomerDto customer = response.getBody();
        assertEquals(1001, customer.getId());
        assertEquals("Robert", customer.getName());
        assertEquals("New-Surname", customer.getSurname());
    }

    @Test
    void updateCustomerFailTest() {
        UpdateCustomerDto updateCustomerDto =
                new UpdateCustomerDto(null,null, "updated@email.com", null);

        ResponseEntity<Map> response =
                restTemplate.exchange(CUSTOMER_URL + "/1001", HttpMethod.PUT, new HttpEntity<>(updateCustomerDto), Map.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Customer with id 1001 not found", response.getBody().get("message"));
    }

    @Test
    @Sql(value = "classpath:customer/controller/deleteCustomerSuccessTest/deleteCustomerSuccessTest.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:customer/controller/cleanUp.sql", executionPhase = AFTER_TEST_METHOD)
    void deleteCustomerSuccessTest() {

        restTemplate.delete(CUSTOMER_URL + "/1001");

        // check call
        ResponseEntity<Map> response =
                restTemplate.getForEntity(CUSTOMER_URL + "/1001", Map.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Customer with id 1001 not found", response.getBody().get("message"));
    }
}
