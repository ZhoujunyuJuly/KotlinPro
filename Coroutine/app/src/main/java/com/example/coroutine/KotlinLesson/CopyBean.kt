package com.example.coroutine.KotlinLesson

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

operator fun String.getValue(value: Any?,property:KProperty<*>) = this
operator fun String.setValue(value: CopyBean?,property: KProperty<*>,str:String){
    println("设置了$str")
    /**
     * 利用了反射，修改private类
     */
    property.javaField?.isAccessible = true
    property.javaField?.set(value,str)
}

class CopyBean {
    var origin : String = "2222333"
    var second : String by origin
}

fun main() {
    val copy = CopyBean()
    println(copy.second)
    copy.second = "23444"
}