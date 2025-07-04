package com.example.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test
import java.io.BufferedReader
import java.io.FileReader

/**
 * 函数说明
 */
class FunIllustration {


    /**
     * 1.use 函数会自动处理文件流关闭的情况，不需要在 finally 里 close()
     * withContext(NonCancelable)不能取消的协程任务：
     *     用于在 finally{} 方法中包裹住 再次进行耗时操作如delay()
     *     本意是这个最后兼容的delay不希望被外部取消
     */
    @Test
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
    @Test
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

}