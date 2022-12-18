package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.request.CourierFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest extends AbstractTest {

    private static String login;

    @BeforeClass
    public static void shouldSuccessfullyCreateCourierForLogin() throws JsonProcessingException {
        login = RandomStringUtils.randomAscii(6);
        var request = CourierFactory.createCourierRequest(login);
        var requestAsJson = objectMapper.writeValueAsString(request);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestAsJson)
                .when()
                .post("/api/v1/courier")
                .then().assertThat()
                .body("ok", is(true))
                .and()
                .statusCode(HTTP_CREATED);
    }

    // тест проверяет логин курьера
    @Test
    public void shouldSuccessfullyLogin() throws JsonProcessingException {
        var request = CourierFactory.loginCourierRequest(login);
        var requestAsJson = objectMapper.writeValueAsString(request);

//        File json = new File("src/test/resources/loginCourier/loginCourier.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestAsJson)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(HTTP_OK);
    }

    // тест проверяет логин курьера без логина
    @Test
    public void shouldBadRequestWithoutLogin() throws JsonProcessingException {

        File json = new File("src/test/resources/loginCourier/loginCourierWithoutLogin.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");

        response.then()
                .log()
                .all()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .body("code", equalTo(400))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    // тест проверяет логин курьера без пароля
    @Test
    public void shouldBadRequestWithoutPassword() throws JsonProcessingException {

        File json = new File("src/test/resources/loginCourier/loginCourierWithoutPassword.json");

        var request = CourierFactory.loginCourierRequest(null);
        var requestAsJson = objectMapper.writeValueAsString(request);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestAsJson)
                .when()
                .post("/api/v1/courier/login");

        response.then()
                .log()
                .all()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .body("code", equalTo(400))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    // тест проверяет неправильно указанный логин курьера
    @Test
    public void shouldNotFoundWrongLogin() {

        File json = new File("src/test/resources/loginCourier/loginCourierWrongLogin.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");

        response.then()
                .log()
                .all()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .body("code", equalTo(404))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

    // тест проверяет неправильно указанный пароль курьера
    @Test
    public void shouldNotFoundWrongPassword() {

        File json = new File("src/test/resources/loginCourier/loginCourierWrongPassword.json");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");

        response.then()
                .log()
                .all()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .body("code", equalTo(404))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }
}
