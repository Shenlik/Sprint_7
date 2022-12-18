package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.respons.ListOrders;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ListOfOrdersTest extends AbstractTest{

    // тест проверяет список заказов
    @Test
    public void returnFields() throws JsonProcessingException {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/v1/orders");

        var ordersJson = response.getBody().asString();
        var orders = objectMapper.readValue(ordersJson, ListOrders.class);

        assertNotNull(orders);
        assertNotEquals(0, orders.getOrders().length);
    }

}
