package com.example.arouter_annotation.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//作用在类上
@Target(ElementType.TYPE)
//周期在编译期
@Retention(RetentionPolicy.CLASS)
public @interface ARouter {
    String path();
    String group() default "";
}
