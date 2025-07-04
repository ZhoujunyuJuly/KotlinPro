package com.example.coroutine.kotlinLesson

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * 重写了String全局的 get和set 函数
 */
operator fun String.getValue(value: Any?,property:KProperty<*>) : String {
    println("获取$value ，property = $property")
    return this
}
operator fun String.setValue(value: Any?,property: KProperty<*>,str:String){
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