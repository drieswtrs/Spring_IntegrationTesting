package com.simpleapi.model;

import org.springframework.stereotype.Component;

import java.beans.JavaBean;
import java.io.Serializable;
import java.util.UUID;

@Component
public class Person {

    private UUID id;
    private String name;

    public Person() {}

    public Person(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String toJson() {
        String json =
                "{\"id\":\""+this.id+"\",\"name\":\"" + this.name + "\"}";

        return json;
    }
}
