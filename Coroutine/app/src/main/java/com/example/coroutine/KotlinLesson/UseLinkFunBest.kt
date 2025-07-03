package com.example.coroutine.KotlinLesson

/**
 * 更高级版
 */
fun <T> start(action: () -> T): T = action()
fun <T, R> T.and(action: T.() -> R): R = action(this)
fun <T> T.end(action: T.() -> Unit) = action(this)

fun main() {
    start {
        "我是"
    }.and {
        this + "富得流油的"
    }.and {
        this + "的200斤"
    }.and {
        this + "的大肚腩"
    }.end {
        println(this + "本腩")
    }


    "dd".apply {
        println("我来了！！")
    }.apply {
        println("我又来了^^")
    }
}

/**
 * apply的源码
 * 高阶函数都要用inline 因为字节码会对lambda优化
 */
private inline fun <T> T.apply(lambda: T.()->Unit) : T {
    //这会改全局
    println("++++")
    lambda()
    return this
}

