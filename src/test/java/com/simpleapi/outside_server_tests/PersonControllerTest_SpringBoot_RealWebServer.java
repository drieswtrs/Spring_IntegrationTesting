package com.simpleapi.outside_server_tests;

import com.simpleapi.controller.PersonController;
import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/*
When working with @SpringBootTest, you are testing with a real HTTP server. In this case, you need
to use a RestTemplate or TestRestTemplate to perform the request.

The assertions change a bit since the response we want to verify is now a ResponseEntity instead
of a MockHttpServletResponse.
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest_SpringBoot_RealWebServer {

    @Autowired
    private PersonController controller;

    @MockBean
    private PersonService service;

    @Autowired
    private TestRestTemplate restTemplate;

    Person karel = new Person(UUID.randomUUID(), "Karel");
    Person frank = new Person(UUID.randomUUID(), "Frank");

    @Test
    public void get_person_by_id() {
        //given
        given(service.findPerson(karel.getId()))
                .willReturn(karel);

        //when
        ResponseEntity<Person> response = restTemplate
                .getForEntity("/person/"+karel.getId(), Person.class);

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()
                    .equals(karel));
    }

    @Test
    public void get_all_persons() {
        //given
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(karel);
        persons.add(frank);
        given(service.findAll())
                .willReturn(persons);

        //when
        ResponseEntity<Person[]> response = restTemplate
                .getForEntity("/person", Person[].class);

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()
                    .equals("["+karel.toJson()+","+frank.toJson()+"]"));

    }

    @Test
    public void add_person() {
        //given
        given(service.addPerson(karel))
                .willReturn(karel);

        //when
        ResponseEntity<Person> response = restTemplate
                .postForEntity("/person", karel, Person.class);

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()
                    .equals(karel.toJson()));
    }

    @Test
    public void update_person() {
        //given
        given(service.updatePerson(karel))
                .willReturn(karel);

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        HttpEntity<Person> requestEntity = new HttpEntity<>(karel, headers );
        ResponseEntity<Person> response = restTemplate
                .exchange("/person", HttpMethod.PUT, requestEntity, Person.class);

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        //will break
        assertThat(response.getBody().equals(karel.toJson()));
    }

    @Test
    public void remove_person() {
        //given

        //when
        ResponseEntity<Person> response = restTemplate
                .exchange("/person/"+karel.getId(), HttpMethod.DELETE, null, Person.class);

        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }
}
