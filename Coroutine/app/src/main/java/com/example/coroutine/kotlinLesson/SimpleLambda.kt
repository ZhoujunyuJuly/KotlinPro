package com.example.coroutine.kotlinLesson

fun main() {
    //1.输入 - > 输出 = {}
    val a: () -> Unit = {
        println("sddd")
    }

    //2.省略输入与输入，以最后一行返回为准
    val b = {
        "df".length
    }

    //3.明确输入与输出，不符时会报异常
    val c: (String) -> Boolean = {
        println(it.plus(2))
        true
    }

    //4.多输入
    val d: (String, Int) -> Boolean = { str: String, num: Int ->
        println(num.plus(2))
        println(str + "qwweer")
        true
    }


    //5.传入一个方法，返回方法的返回值,可以链式调用
    val e = { f: (String) -> Boolean, input: String ->
        f(input)
    }

    //6.老师举的例子，跟我上面的一样
    val f = { useName: String, a: (Boolean) -> Boolean ->
        println("f:userName = $useName , isEqual = ${a(useName == "aaa")}")
    }

    val g = { str:String ->
        {
                n:Int->
            println("$str 是字符串吗？$n 是数字")
        }
    }

    /**
     * 总结：1.由于可以根据最后一行返回类型，所以无强制校验的情况可以不写返回类型
     *      2.单输入用it，多输入另写
     *      3.本质-> a = {}
     *        升级-> b :(A)->B = {}
     */


    a()
    println(b())
    c("2")
    d("3", 6)
    e({
        println("链式调用，$it")
        it.isNotEmpty()
    }, "asldfasfd")
    f("aaa") {
        println("进到a的实现了:result = $it")
        it
    }
    g("ddd")(8)
}