package com.simpleapi.inside_server_tests;

import com.simpleapi.controller.PersonController;
import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

/*
Spring MockMvc example with WebApplicationContext:
This strategy can be used to write Unit Tests for a Controller. (inside-server strategy).
The responses we're verifying are fake, there is no web server involved in this test.

The main advantage of this approach is that we don't need to explicitly load the surrounding
application logic.

There is a small transition from Unit testing to Integration testing, since we are testing
the filter and the controller advices, or any other logic participating in the request-response
process (we will ge them automatically injected in our tests).
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest_SpringBoot_MockMvcWithContext {

    @Autowired
    private MockMvc mvc;

    // Overriding beans for testing using MockBean
    @MockBean
    private PersonService service;

    Person karel = new Person(UUID.randomUUID(), "Karel");
    Person frank = new Person(UUID.randomUUID(), "Frank");

    @Test
    public void get_person_by_id() throws Exception {
        //given
        // Defines what the mocked personService class will return
        given(service.findPerson(karel.getId()))
                .willReturn(karel);

        //when
        // Request under test
        MockHttpServletResponse response = mvc.perform(
                get("/person/{id}", karel.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()
                    .replaceAll("\\s+", ""))
                .isEqualTo(karel.toJson());
    }

    @Test
    public void get_all_persons() throws Exception {
        // given
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(karel);
        persons.add(frank);
        given(service.findAll())
                .willReturn(persons);

        //when
        MockHttpServletResponse response = mvc.perform(
                get("/person")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()
                .replaceAll("\\s+", ""))
                .isEqualTo("["+karel.toJson()+","+frank.toJson()+"]");
    }

    @Test
    public void add_person() throws Exception {
        //given
        given(service.addPerson(karel))
                .willReturn(karel);

        //when
        MockHttpServletResponse response = mvc.perform(
                post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(karel.toJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void update_person() throws Exception {
        //given
        given(service.updatePerson(karel))
                .willReturn(karel);

        //when
        MockHttpServletResponse response = mvc.perform(
                put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(karel.toJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

        //will break
        assertThat(response.getContentAsString())
                .isEqualTo(karel.toJson());
    }

    @Test
    public void delete_person() throws Exception {
        // It's not possible to define the ".willReturn()" of a void method

        //when
        MockHttpServletResponse response = mvc.perform(
                delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(karel.toJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }
}
