package com.howtodoinjava.demo.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Document		//this annotation is the Mongo equivalent of @Entity in JPA
public class Employee {
 
    @Id
    int id;
    String name;
    long salary;
 
    //Getters and setters
 
    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + "]";
    }
}
