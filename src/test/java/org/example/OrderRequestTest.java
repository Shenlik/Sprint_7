package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.mapping.GsonMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.mapper.factory.DefaultGsonObjectMapperFactory;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import org.apache.http.HttpStatus;
import org.example.request.OrderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

@RunWith(Parameterized.class)
public class OrderRequestTest extends AbstractTest {

    private final String[] color;

    public OrderRequestTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object [][] getCorrectHasManeValue() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"GREY", "BLACK"}},
                {new String[]{}}
        };
    }
    @Test
    public void CreateOrderTest() throws Exception {
        var request = OrderFactory.createOrder(color);
        var requestAsJson= objectMapper.writeValueAsString(request);
        var response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestAsJson)
                .when()
                .post("/api/v1/orders");

        response.then().assertThat()
                .statusCode(HTTP_CREATED);
    }
}
