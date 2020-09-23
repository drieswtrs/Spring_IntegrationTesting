package com.simpleapi.RestAssured;

import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringExampleTest_RestAssured {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

    Person karel = new Person(UUID.randomUUID(), "karel");
    Person frank = new Person(UUID.randomUUID(), "frank");

    @Before
    public void setup() {
        reqSpec = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        RestAssured.requestSpecification = reqSpec;
        RestAssured.responseSpecification = resSpec;
    }

    @Test
    public void add_person() {


        //when
        Response response = RestAssured
            .given()
                .body(frank)
            .when()
                .post("/person");

        //then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());

        assertThat(response.getBody().asString())
                .isEqualTo(frank.toJson());
    }

    @Test
    public void get_person_by_id() {

        RestAssured
                .given()
                .body(karel)
                .post("/person");

        //when
        Response response =
                RestAssured.get("/person/{id}", karel.getId());

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());

        assertThat(response.body().asString()
                .replaceAll("\\s+", ""))
                .isEqualTo(karel.toJson());
    }

    @Test
    public void get_all_persons() {

        RestAssured
                .given()
                .body(karel)
                .post("/person");

        RestAssured
                .given()
                .body(frank)
                .post("/person");


        //when
        Response response =
                RestAssured.get("/person");

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());

        assertThat(response.getBody().asString())
                .isEqualTo("["+karel.toJson()+","+frank.toJson()+"]");
    }

    @Test
    public void update_person() {

        //when
        Response response = RestAssured
            .given()
                .body(frank)
            .when()
                .put("/person");

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());

        assertThat(response.body().asString()
                .replaceAll("\\s+", ""))
                .isEqualTo(frank.toJson());
    }

    @Test
    public void delete_person() {

        //when
        Response response = RestAssured
            .given()
            .when()
                .delete("/person/{id}", karel.getId());

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
    }

}
