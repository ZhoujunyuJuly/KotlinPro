package com.example.coroutine.KotlinLesson

import java.lang.reflect.Array.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

interface Animal{
    fun name()
}

class Human:Animal{
    var origin = 3.14f
        /**
         * 1.重写var类型的set和get
         */
        set(v){
            println("设置当前值为 $v")
        }
        get(){
            println("获取当前值为 $field")
            return field
        }

    /**
     * 2.将origin委托给use,共用set和get方法，用于版本升级
     */
    private var use by ::origin

    /**
     * 3.懒加载，当调用的时候才真正走方法。当作单例
     */
    private val respond : String by lazy {download()}
    override fun name() {
        use = 3f;
        println(origin)

        println("点击下载")
        //Thread.sleep(3000)
        /**
         * 4.此时才调用download方法
         */
        println(respond)
        /**
         * 5.只有首次调用才会加载方法，后续使用会将值存在respond中，不会再调用方法
         */
        println(respond)
        println(respond)

    }

    fun download() : String{
        println("真正开始下载-----")
        return "下载完成"
    }
}

class Dog:Animal{
    override fun name() {
        println("dog")
    }
}

class Bird:Animal{
    override fun name() {
        println("bird")
    }
}

/**
 * 代理
 * 1.使用by可以让StartWork的实现委托给human传入的参数
 * 2.委托类必须是接口，其实就是代理，java的代理也是通过接口实现的
 */
class StartWork(human:Animal):Animal by human

fun main() {
    StartWork(Human()).name()
    StartWork(Dog()).name()
    StartWork(Bird()).name()

    val workMate = WorkMate(22)
    workMate.boy
    workMate.autoBoy

}


/**
 * 6.自定义代理/委托
 */
class WorkMate<T>(value: T){
    var boy by Work(value)
    var autoBoy by AutoWork(value)
}

/**
 * 7.引入泛型，自己实现
 */
class Work<T>(var me:T){
    //property是属性名
    operator fun getValue(owner:T,property:KProperty<*>):T{
        println("工作交给你了 = $me")
        return me
    }

    operator fun setValue(owner: T,property:KProperty<*>,value:T){
        println("接新任务了 = $value")
        me = value
    }
}

/**
 * 通过继承通用类，自动实现get/set
 */
class AutoWork<T>(var me:T):ReadWriteProperty<T,T>{
    override fun getValue(thisRef: T, property: KProperty<*>): T {
        println("机器人：工作交给你了 = $me")
        return me
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: T) {
        println("机器人：接新任务了 = $value")
        me = value
    }
}

