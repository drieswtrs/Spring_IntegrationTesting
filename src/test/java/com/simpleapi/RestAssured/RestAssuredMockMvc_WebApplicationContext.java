package com.simpleapi.RestAssured;

import com.simpleapi.WebConfig;
import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@SpringBootTest
public class RestAssuredMockMvc_WebApplicationContext {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PersonService service;

    Person karel = new Person(UUID.randomUUID(), "karel");
    Person frank = new Person(UUID.randomUUID(), "frank");

    @Before
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(context);

        requestSpecification = new MockMvcRequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
    }

    @Test
    public void get_person_by_id() {
        given(service.findPerson(karel.getId()))
                .willReturn(karel);

        MockMvcResponse response = RestAssuredMockMvc
            .given()
                .log().all()
            .when()
                .get("/person/{id}", karel.getId());

        System.out.println("> response: \n" + response.body().asString());

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString())
                .isEqualTo(karel.toJson());
    }

    @Test
    public void get_all_persons() {
        Collection<Person> persons = new ArrayList<>();
        persons.add(karel);
        persons.add(frank);
        given(service.findAll())
                .willReturn(persons);

        MockMvcResponse response = RestAssuredMockMvc
            .given()
                .log().all()
            .when()
                .get("/person");

        System.out.println("> response: \n" + response.body().asString());

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString().replaceAll("\\s+", ""))
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
        given(service.updatePerson(karel))
                .willReturn(karel);

        MockMvcResponse response = RestAssuredMockMvc
            .given()
                .log().all()
                .body(karel)
            .when()
                .put("/person");

        assertThat(response.statusCode())
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
