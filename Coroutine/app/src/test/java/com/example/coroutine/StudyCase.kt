package com.example.coroutine

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Test
import java.io.BufferedReader
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.measureTime

class Coroutine{
    /**
     * 1.runBlocking 会阻塞主线程
     *   coroutineScope 不会阻塞当前线程，只会挂起。一个协程失败了，所有协程都会失败
     *   supervisorScope 一个失败，不会影响其他
     */
    @Test
    fun runBlocking() = runBlocking {
        val first = launch {
            delay(2000)
            printInfo("第一步")
        }

        //launch是立即执行
        //first.join()

        val second = async {
            delay(2000)
            printInfo("第二步")
            "返回结果"
        }

        println(second.await())
    }

    /**
     * 如果是SupervisorJob类型，一个协程异常不会阻止兄弟协程运行
     * 如果是 Job 类型，一个协程异常，兄弟协程都终止
     */
    @Test
    fun supervisorJob() = runBlocking{
        val coroutineScope = CoroutineScope(SupervisorJob())
        val job1 = coroutineScope.launch {
            printInfo("job1")
            throw IllegalArgumentException()
        }
        val job2 = coroutineScope.launch {
            try {
                delay(Long.MAX_VALUE)
            }catch (e : Exception){
                printInfo("job结束 , $e")
            }
        }
        joinAll(job1,job2)
    }

    /**
     * 2.join关键字，做事但不交代结果
     */
    @Test
    fun runJoin() = runBlocking {
        val first = launch {
            printInfo("第一步")
            delay(2000)
        }

        /**
         * 加join会等待线程1执行完，再跑后面的。同时不会返回结果
         * launch没有await方法
         */
        printInfo("join的输出结果 = ${first.join()}")

        val second = launch {
            printInfo("第二步")
            delay(500)
            "返回结果"
        }
        val third = launch {
            printInfo("第三步")
            delay(500)
            "返回结果"
        }
    }


    /**
     * 3.wait关键字，做事但交代结果
     */
    @Test
    fun runWait() = runBlocking {
        printInfo("runWait------")

        val first = async {
            delay(2000)
            printInfo("第一步")
            "返回结果"
        }

        /**
         * await会等到拿到结果
         * 只有async有await方法，并且没有join方法
         */
        printInfo("join的输出结果 = ${first.await()}")

        val second = async {
            delay(500)
            printInfo("第二步")
            "返回结果"
        }
        val third = async {
            delay(500)
            printInfo("第三步")
            "返回结果"
        }
    }


    /**
     * 4.在挂起协程内执行两个耗时操作，是串行
     */
    @Test
    fun calculate() = runBlocking{
        val time = measureTime {
            val one = suspend {
                // 挂起函数 delay 只能在挂起上下文里调用
                // 要么是suspend，要么是协程做红域
                delay(1000)
                12
            }
            val two = suspend {
                delay(1000)
                3
            }
            val result = one() + two()
            /**
             * 时间 = 2.030166542s
             */
            println("结果为 = $result")
        }
        println("时间 = $time")
    }

    /**
     * 5.在挂起协程内【异步】执行两个耗时操作，是并行
     */
    @Test
    fun calculateAsync() = runBlocking(){
        val time = measureTime {
            val one = async {
                delay(1000)
                11
            }

            val two = async {
                delay(1000)
                2
            }
            println("结果为 = ${one.await() + two.await()}")
        }
        println("时间 = $time")
    }

    /**
     * 6.其他写法，纠结了我很久的
     */
    @Test
    fun calculateAsync2() = runBlocking(){
        val time = measureTime {
            val one = async {
                //传方法而不是直接调用
                runNum()
            }

            val two = async {
                runNum()
                2
            }
            println("结果为 = ${one.await() + two.await()}")
        }
        println("时间 = $time")
    }

    /**
     * 【重要‼️ 】
     * 问题:为什么上一种情况可以直接在async运行，调用方法却要加suspend?
     * 1.async代码本身是挂起上下文
     *   async 参数是一个 suspend 函数类型的 lambda，
     *   在其代码块中可以直接调用挂起函数，编译器知道这个代码块会在协程中执行
     * 2.delay()是挂起函数，必须运行在协程上下文中
     *   delay() 会暂停当前协程的执行，而不会阻塞线程，因此它必须运行在协程上下文中
     */
    suspend fun runNum():Int{
        delay(1000)
        return 11
    }

    /**
     * 7.协程启动模式‼️
     * - 🌟DEFAULT:立即调度，如果调度前被取消，直接进入取消响应状态
     *   (1)马上执行取消任务——还没进入协程：
     *      准备执行取消 ,19:37:32:986
     *      执行取消完成 ,19:37:32:991
     *      BUILD SUCCESSFUL in 730ms
     *   (2)延迟一秒取消任务——进入了再取消
     *      准备执行取消 ,19:38:29:179
     *      进入协程内部 ,19:38:29:185
     *      执行取消完成 ,19:38:30:194
     *      BUILD SUCCESSFUL in 1s
     *
     * - 🌟ATOMIC:立即调度，执行到第一个挂起点前不取消
     *  (1)马上执行取消——必须先进入协程，直到遇到第一个挂起点再取消
     *      准备执行取消 ,19:40:52:208
     *      执行取消完成 ,19:40:52:213
     *      进入协程内部 ,19:40:52:214
     *      BUILD SUCCESSFUL in 664ms
     *
     * - 🌟LAZY:其他都是立刻调度，这个只有start/join/await才调度。调取前取消直接进入异常结束状态
     *
     * - 🌟UNDISPATCHED:立即在当前函数调用栈执行，即不切换线程，进来是啥就是啥
     */
    @Test
    fun mode() = runBlocking(){
        val job = launch(start = CoroutineStart.ATOMIC) {
            printInfo("进入协程内部")
            delay(2000)
            println("完成处理")
        }
        printInfo("准备执行取消")
        //delay(1000)
        job.cancel()
        printInfo("执行取消完成")
    }


    /**
     * 8.大写的 GlobalScope 和 CoroutineScope ，与调用coroutineScope方法的区别：
     *   大写的是自己单独管理的独立 Context ，作用域不同，当 runBlocking 协程完成时，
     *   不会等待全局协程完成，会结束掉当前协程
     */
    @Test
    fun innerBigScope() = runBlocking{
        val scope = GlobalScope.launch {
            try {
                printInfo("执行")
                delay(1000)
                printInfo("执行结束")
            }catch (e:Exception){
                //e.printStackTrace()
                printInfo("$e")
            }
        }
        //要强制等待才会执行
        //如果放到前面不会抛出异常，是因为不写join的时候没有输出打印，因为协程并不在当前协程里执行
        //当前协程不需要等待它的执行，方法完毕就退出程序了。因为实际是两条独立的线程
        //加了这个join就是当前线程等待全局协程完成，既然已经完成，那取消也不会有异常
        //把join放最后是因为要强制等待全局协程完成，这样就会捕捉到取消异常
        //如果在cancel后面再延迟200ms，也是可以捕捉到的

        //scope.join()
        printInfo("准备取消")
        scope.cancel(CancellationException("取消异常"))
        printInfo("取消完成")
        scope.join()
        delay(3000)
    }

    /**
     * 9.与8对照，用的是作用域内的协程方法，它继承了 runBlocking 的协程上下文
     *   在同一个作用域，会立即执行
     */
    @Test
    fun innerFunScope() = runBlocking{
        coroutineScope {
            printInfo("执行")
            delay(1000)
            printInfo("执行结束")
        }
    }


    /**
     * 10.CPU 密集型协程不会在主线程处理 cancel
     * 可以使用 ensureActive() 或 yield() 感知协程当前状态
     */
    @Test
    fun cancelCpuMulti() = runBlocking{
        var nextTime = System.currentTimeMillis()

        /**
         * ‼️这里设置线程调度器，默认是继承父类 runBlocking 的上下文，即主线程
         * 只有在后台线程中才能实时感知 active 变化，在主线程中无法实时处理 cancel 状态，只能增加 yield()
         *
         * 当使用 Dispatchers.Default（即协程在后台线程池中执行），取消信号会比在主线程（runBlocking）中更快地被传播和感知
         * 所以在 ensureActive() 中更容易观察到 isActive == false。
         *
         * 而如果不指定调度器（默认在 runBlocking 的上下文中），协程运行在主线程上，由于协程是忙等（CPU密集循环）+ 没挂起点(yield是挂起点)
         * 调度器没机会调度 cancel 信号，所以 isActive 就一直是 true。
         */
        val job = launch (){
            var i = 1
            while (i <= 10) {
                ensureActive()
                //yield()
                if (System.currentTimeMillis() > nextTime) {
                    printInfo("job:${i++},activie = ${isActive}")
                    nextTime += 500
                }
            }
        }
        delay(1300)
        job.cancel()
    }

}

fun printInfo(info:String) {
    val timestamp = System.currentTimeMillis()
    val sdf = SimpleDateFormat("HH:mm:ss:SS", Locale.getDefault())
    println("$info ,${sdf.format(Date(timestamp))}")
}
