package com.simpleapi.RestAssured;

import com.simpleapi.controller.PersonController;
import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


public class SpringExampleTest_RestAssured {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

    Person karel = new Person(UUID.randomUUID(), "karel");
    Person frank = new Person(UUID.randomUUID(), "frank");

    @Before
    public static void setup() {
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

    @org.junit.Test
    public void get_person_by_id() throws Exception {

        //when
        // Request under test
        MockMvcResponse response =
                RestAssuredMockMvc.get("/person/{id}", karel.getId());

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()
                .replaceAll("\\s+", ""))
                .isEqualTo(karel.toJson());
    }

    @org.junit.Test
    public void get_all_persons() {

        //when
        MockMvcResponse response =
                RestAssuredMockMvc.get("/person");

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().asString())
                .isEqualTo("["+karel.toJson()+","+frank.toJson()+"]");
    }

    @org.junit.Test
    public void add_person() {

        //when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .body(karel)
                .when()
                .post("/person");

        //then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());

        // Will fail
        // MockMvc does not return response body when put/post/delete/...
        assertThat(response.getBody().asString())
                .isEqualTo(karel.toJson());
    }

    @org.junit.Test
    public void update_person() {

        //when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .body(karel)
                .when()
                .put("/person");

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void delete_person() {

        //when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .body(karel)
                .when()
                .put("/person");

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
    }

}
