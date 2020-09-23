package com.simpleapi.RestAssured;

import com.simpleapi.controller.PersonController;
import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.requestSpecification;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.post;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.put;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.delete;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
public class RestAssuredMockMvc_MockMvc {

    @Autowired
    private MockMvc mvc;

    // Overriding beans for testing using MockBean
    @MockBean
    private PersonService service;

    Person karel = new Person(UUID.randomUUID(), "Karel");
    Person frank = new Person(UUID.randomUUID(), "Frank");

    @Before
    public void setup() {
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void get_person_by_id() throws Exception {
        //given
        // Defines what the mocked personService class will return
        given(service.findPerson(karel.getId()))
                .willReturn(karel);

        //when
        // Request under test
        MockMvcResponse response =
                get("/person/{id}", karel.getId());

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()
                .replaceAll("\\s+", ""))
                .isEqualTo(karel.toJson());
    }

    @Test
    public void get_all_persons() {

        //given
        Collection<Person> persons = new ArrayList<>();
        persons.add(karel);
        persons.add(frank);
        given(service.findAll())
                .willReturn(persons);

        //when
        MockMvcResponse response =
                get("/person");

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().asString())
                .isEqualTo("["+karel.toJson()+","+frank.toJson()+"]");
    }

    @Test
    public void add_person() {

        //given
        given(service.addPerson(karel))
                .willReturn(karel);

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

    @Test
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
            .when()
                .delete("/person/{id}", karel.getId());

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK.value());
    }
}
