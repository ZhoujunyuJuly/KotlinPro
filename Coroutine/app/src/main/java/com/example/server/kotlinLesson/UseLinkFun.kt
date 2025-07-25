package com.example.server.kotlinLesson

/**
 * 登峰造极的链式调用
 */
class Helper<T>(private var item:T){
    fun <R>map(action:T.()->R):Helper<R> = Helper(action(item))
    fun consumer(action:T.()->Unit) = action(item)
}

fun <T>create(action:()->T) : Helper<T> =  Helper(action())

fun main() {
    create {
        "aaa"
    }.map{
        this + "bbb"
    }.map {
        this + "ccc"
    }.consumer {
        println(this + "dd")
    }
}