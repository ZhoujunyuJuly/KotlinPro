package com.example.javamodule.net.interview.copy.deepCopy;

import java.io.Serializable;

public class Person implements Serializable {
    public String name;
    public int age;
    public Address address;

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public static class Address implements Serializable{
        String path;

        public Address(String path) {
            this.path = path;
        }
    }
}
