package com.simpleapi.controller;

import com.simpleapi.model.Person;
import com.simpleapi.service.IPersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/person")
public class PersonController {

    private final IPersonService personService;

    @Autowired
    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{person_id}")
    public @ResponseBody Person getPerson(@PathVariable("person_id") UUID id) {
        return personService.findPerson(id);
    }

    @GetMapping()
    public @ResponseBody Collection<Person> getAllPersons() {
        return personService.findAll();
    }

    @PostMapping()
    public @ResponseBody Person addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @PutMapping()
    public @ResponseBody Person updatePerson(@RequestBody Person person) {
        return personService.updatePerson(person);
    }

    @DeleteMapping("/{person_id}")
    public void removePerson(@PathVariable("person_id") UUID id) {
        this.personService.removePerson(id);
    }
}
