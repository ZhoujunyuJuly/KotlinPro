package com.example.server.kotlinLesson

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * 重写了String全局的 get和set 函数
 */
operator fun String.getValue(value: Any?,property:KProperty<*>) : String {
    println("获取$this ，property = $property")
    return this
}
operator fun String.setValue(value: Any?,property: KProperty<*>,str:String){
    println("设置了$str,name = $property")
    /**
     * 利用了反射，修改private类
     */
    property.javaField?.isAccessible = true
    property.javaField?.set(value,str)
}

class CopyBean {
    var origin : String = "2222333"

    /**
     * by 这里委托，只是委托了origin的get/set属性，并不是引用了origin
     * 只是初始值用了origin的值，后续origin发生修改时，second并不通知
     */
    var second : String by origin
}

fun main() {
    val copy = CopyBean()
    copy.origin = "修改了"
    println(copy.second)
    copy.second = "23444"
}