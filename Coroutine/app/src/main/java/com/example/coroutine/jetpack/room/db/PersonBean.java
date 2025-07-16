package com.example.coroutine.jetpack.room.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 🌟一定要设置主键，并且如果主键设置的自增长 autoGenerate = true，就可以不用关心id
 * 🌟@Ignore 是给数据库区分用户自定义的构造函数，Room会用默认的那个构建数据库表
 * 🌟版本升级：要同步增加 @ColumnInfo 对应字段，不然数据库建表有问题
 */
@Entity
public class PersonBean {
    /**
     * 🌟设置自增长，才能只传其他参数
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id",typeAffinity = ColumnInfo.INTEGER)
    public int id;

    @ColumnInfo(name = "name",typeAffinity = ColumnInfo.TEXT)
    public String name;

    @ColumnInfo(name = "age",typeAffinity = ColumnInfo.INTEGER)
    public int age;

    @ColumnInfo(name = "sex",typeAffinity = ColumnInfo.TEXT)
    public String sex;

    public PersonBean(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Ignore
    public PersonBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Ignore
    public PersonBean(int id) {
        this.id = id;
    }

    @Ignore
    public static List<PersonBean> getDatas(int count){
        List<PersonBean> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PersonBean bean = new PersonBean("Jack" + i,new Random().nextInt(50 - 15) + 15);
            list.add(bean);
        }
        return list;
    }
}
