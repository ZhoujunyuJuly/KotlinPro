package com.example.javamodule.net.interview.copy.deepCopy;

import com.example.javamodule.net.interview.copy.deepCopy.Person;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Handler;

public class Main {

    public static <T extends Serializable> T deepCopy(T obj){
        /**
         * 🧠如何记这个复杂的数据转换呢？
         * 🌟ByteArrayOut(In)putStream 是存放数据的字节数组
         * 🌟ObjectOut(In)putStream 是一个操作机，我理解是将数据通过一层字节流转换后就是新的对象了
         *          打碎机：将对象->字节流
         *          重组机：将字节->对象流
         */
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(obj);


            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (T)ois.readObject();
        }catch (Exception e){
            System.out.println(e);
            return obj;
        }
    }

    public static void main(String[] args) {
        Person person1 = new Person("andrew",12,new Person.Address("beijing"));
        Person person2 = deepCopy(person1);
        person2.address.path = "shanghai";
        System.out.println("person1 = " + person1.address.path + " , person2 = "
                + person2.address.path);
    }
}
