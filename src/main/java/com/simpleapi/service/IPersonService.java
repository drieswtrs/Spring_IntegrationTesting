package com.simpleapi.service;

import com.simpleapi.model.Person;

import java.util.Collection;
import java.util.UUID;

public interface IPersonService {

    Person addPerson(Person person);

    Person updatePerson(Person person);

    void removePerson(UUID id);

    Collection<Person> findAll();

    Person findPerson(UUID id);

}
