package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.request.CourierFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends AbstractTest {


    // тест проверяет успешное создание курьера
    @Test
    public void shouldSuccessfullyCreate() throws JsonProcessingException {
        var login = RandomStringUtils.randomAscii(6);
        var request = CourierFactory.createCourierRequest(login);
        var requestAsJson = objectMapper.writeValueAsString(request);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestAsJson)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat()
                .body("ok", is(true))
                .and()
                .statusCode(HTTP_CREATED);
    }

    // тест проверяет создание двух одинаковых курьеров
    @Test
    public void shouldReturnConflict() {

        File json = new File("src/test/resources/successfulCreationOfCourier/newCourier1.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");

        response.then()
                .log()
                .all()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .body("code", equalTo(409))
                .and()
                .statusCode(HTTP_CONFLICT);
    }

    // тест проверяет обязательность поля login
    @Test
    public void shouldReturnBadRequestWithoutLogin() {

        File json = new File("src/test/resources/withoutRequiredField/newCourierWithoutLogin.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");

        response.then()
                .log()
                .all()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .body("code", equalTo(400))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    // тест проверяет обязательность поля password
    @Test
    public void shouldReturnBadRequestWithoutPassword() {

        File json = new File("src/test/resources/withoutRequiredField/newCourierWithoutPassword.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");

        response.then()
                .log()
                .all()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .body("code", equalTo(400))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }


}