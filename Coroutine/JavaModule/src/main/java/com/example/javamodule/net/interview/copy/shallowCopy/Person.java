package com.example.javamodule.net.interview.copy.shallowCopy;

public class Person implements Cloneable{
    public String name;
    public int age;
    public Address address;

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person person = (Person) super.clone();
        if( person.address != null ){
            person.address = (Address) this.address.clone();
        }
        return person;
    }

    public static class Address implements Cloneable{
        String path;

        public Address(String path) {
            this.path = path;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
