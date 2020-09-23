package com.simpleapi;


import com.simpleapi.controller.PersonController;
import com.simpleapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PersonApplication {

    private static ApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(PersonApplication.class, args);

        checkBeansPresence("person", "personService", "personController", "personApplication");
    }

    private static void checkBeansPresence(String... beans) {
        System.out.println("Check beans presence:");
        for (String beanName : beans) {
            System.out.println("> " + beanName + ": " + context.containsBean(beanName));
        }
    }

}
