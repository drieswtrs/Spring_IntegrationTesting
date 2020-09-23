package com.simpleapi.service;

import com.simpleapi.model.Person;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService implements IPersonService {

    private Map<UUID, Person> personList;

    public PersonService() {
        this.personList = new LinkedHashMap<>();
    }

    public Person addPerson(Person person) {
        this.personList.put(person.getId(), person);
        return person;
    }

    public Person updatePerson(Person person) {
        return this.addPerson(person);
    }

    public void removePerson(UUID id) {
        this.personList.remove(id);
    }

    public Collection<Person> findAll() {
        return new ArrayList<Person>(this.personList.values());
    }

    public Person findPerson(UUID id) {
        return this.personList.get(id);
    }
}
