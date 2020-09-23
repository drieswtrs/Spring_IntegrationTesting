package com.simpleapi.inside_server_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleapi.controller.PersonController;
import com.simpleapi.model.Person;
import com.simpleapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest_SpringBoot_MockMVCRequestBuilders {

    @Autowired
    private MockMvc mvc;

    // Overriding beans for testing using MockBean
    @MockBean
    private PersonService service;

    Person karel = new Person(UUID.randomUUID(), "Karel");

    @Test
    public void get_person_by_id() throws Exception {

        // Defines what the mocked personService class will return
        given(service.findPerson(karel.getId()))
                .willReturn(karel);

        // Request under test
        mvc.perform(MockMvcRequestBuilders
                .get("/person/{id}", karel.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // assert response body with object json
            .andExpect(MockMvcResultMatchers.content().json(karel.toJson()))
            .andDo(print());
    }

    @Test
    public void get_all_persons() throws Exception {

        ArrayList<Person> list = new ArrayList<Person>();
        list.add(karel);

        given(service.findAll())
                .willReturn(list);

        mvc.perform(MockMvcRequestBuilders
                .get("/person")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void add_person() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .post("/person")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(karel))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void update_person() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .put("/person")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void delete_person() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .delete("/person/{id}", karel.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
