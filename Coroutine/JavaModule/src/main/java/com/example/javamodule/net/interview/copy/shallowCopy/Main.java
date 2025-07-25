package com.example.javamodule.net.interview.copy.shallowCopy;

public class Main {

    public static void main(String[] args) throws Exception {
        Person person1 = new Person("andrew",12,new Person.Address("beijing"));
        Person person2 = (Person) person1.clone();
        person2.address.path = "shanghai";
        /**
         * 浅拷贝
         * 如果只是实现了外层的浅拷贝，发现输出🌟：person1 = shanghai , person2 = shanghai
         * 用的同一个Address对象
         */
        System.out.println("person1 = " + person1.address.path + " , person2 = "
                + person2.address.path);

        /**
         * 深拷贝
         * 继续将Address实现浅拷贝，终于两个Address不同了
         * 输出🌟：person1 = beijing , person2 = shanghai
         */
        System.out.println("person1 = " + person1.address.path + " , person2 = "
                + person2.address.path);

    }
}
