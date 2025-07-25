package com.example.server

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.io.BufferedReader
import java.io.FileReader
import java.util.InputMismatchException

/**
 * 函数说明
 */
class FunIllustration {


    /**
     * 1.use 函数会自动处理文件流关闭的情况，不需要在 finally 里 close()
     * withContext(NonCancelable)不能取消的协程任务：
     *     用于在 finally{} 方法中包裹住 再次进行的耗时操作如delay()
     *     本意是这个最后兼容处理的delay不希望被外部取消
     */
    //@Test
    fun useFun() = runBlocking{
        BufferedReader(FileReader("D:\\I have a dream.txt")).use {
            var line :String?
            while (true){
                line = it.readLine() ?: break
                printInfo(line)
            }
        }
    }

    /**
     * 2.withTimeout 方法，设置超时时间，可以返回正常/异常结果
     */
    //@Test
    fun timeoutFun(): Unit = runBlocking {
        val timeout = withTimeoutOrNull(1000) {
            repeat(100) {
                delay(1000)
                printInfo("循环...")
            }
            "result"
        } ?: "NoResult"

        println("结果 = $timeout")
    }


    /**
     *  3.异常捕获
     * ❗只有 根协程 抛出的异常，才会被 CoroutineExceptionHandler 捕获。
     * 所以一定要用 GlobalScope.launch
     * 如果用 launch{}放子线程异常 ：
     *      - 要么单独对每个子线程加上 handler ,因为子线程会隐藏异常向上传播
     *      - 要么写 supervisorJob 让子线程的异常不要互相影响，这样父类有机会捕获异常
     */
    //@Test
    fun exception() = runBlocking {
        val handler = CoroutineExceptionHandler{_,exception->
            printInfo("Catch $exception : ${exception.suppressed.contentToString()}")
        }

        val job = GlobalScope.launch(handler) {
            launch {
                printInfo("job1")
                throw IllegalArgumentException()
            }

            launch {
                printInfo("job2")
                throw IndexOutOfBoundsException()
            }

            launch {
                printInfo("job3")
                throw ArrayIndexOutOfBoundsException()
            }

            launch {
                printInfo("job4")
                throw InputMismatchException()
            }
        }
        job.join()
    }

}