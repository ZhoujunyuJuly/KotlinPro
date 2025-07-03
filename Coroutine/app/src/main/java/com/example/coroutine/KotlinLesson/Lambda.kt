package com.example.coroutine.KotlinLesson


fun main() {
    //stringModify()
    intModify()
}

fun stringModify(){
    val str : String.() -> Unit = {
        println(this.show())
    }
    str(" ff  ff  f 3 34 566g hgasg ")
}

fun intModify(){
    var num = 3
    println(num.modifyInt(5))

    val myModifyInt:Int.(Int)-> String = {
        "myModify -> $this + $it = ${this + it}"
    }

    /**
     * 3.这样两种写法都可以，会默认把两个参数放在后面
     */
    println(7.myModifyInt(9))
    println(myModifyInt(7,8))

    //虽然没看明白
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
 */
fun Int.modifyInt(it:Int): String {
    return "$this + $it = ${this + it}"
}

/**
 * 4.run操作符，方法返回什么就给上层什么
 */
fun operate():Char = run {
    'A'
}


/**
 * 5.接受一个f类型的函数，再把f函数返回回去
 */
fun returnFun(f: (String) -> Boolean): (String) -> Boolean {
    return f
}

fun inputFun() : () -> Boolean = {true}

fun<T> map(action:(String)->T){

}

