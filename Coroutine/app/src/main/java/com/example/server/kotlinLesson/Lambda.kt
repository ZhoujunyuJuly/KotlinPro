package com.example.server.kotlinLesson


fun main() {
    //stringModify()
    intModify()
}

fun stringModify(){
    /**
     * 函数类型：扩展在 String 上的函数
     */
    val str : String.() -> Unit = {
        println(this.show())
    }
    str(" ff  ff  f 3 34 566g hgasg ")
}

fun intModify(){
    var num = 3
    println(num.modifyInt(5))

    //扩展在Int上的函数
    val myModifyInt:Int.(Int)-> String = {
        "myModify -> $this + $it = ${this + it}"
    }

    /**
     * 3.这样两种写法都可以，会默认把两个参数放在后面
     */
    println(7.myModifyInt(9))
    println(myModifyInt(7,8))

    //inputFun()是调用该方法，得到()->Boolean的返回函数
    //inputFun()()是再次调用返回函数的方法，由于是无入参所以是()，最后得到返回结果boolean
    inputFun()()

}

/**
 * 1.直接修改对String增加基类方法
 */
fun String.show() : String {
    return this.replace(" ","aaa")
}

/**
 * 2.传入参数修改int方法
 *  扩展在Int上的函数
 */
fun Int.modifyInt(it:Int): String {
    return "$this + $it = ${this + it}"
}

/**
 * 4.run操作符，返回最后一行的结果
 *   接收一个无入参但有返回结果的函数，将作为R的返回
 *   内联函数：public inline fun <R> run(block: () -> R): R
 */
fun operate():Char = run {
    //run是立刻执行的，如果是{}要到调用才会执行-》如果不是方法，而是属性的话
    'A'
}


/**
 * 5.接受一个f类型的函数，再把f函数返回回去
 */
fun returnFun(f: (String) -> Boolean): (String) -> Boolean {
    return f
}


/**
 * 这个冒号：后面跟的是返回值，所以返回值为一个函数(无入参，返回一个boolean)
 */
fun inputFun() : () -> Boolean = {true}

fun<T> map(action:(String)->T){

}

