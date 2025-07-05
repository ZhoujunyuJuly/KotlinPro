package com.example.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.measureTime


class Flow {

    //序列
    val sequence = sequence {
        for (i in 1..3) {
            //阻塞
            Thread.sleep(1000)
            yield(i)
        }
    }

    /**
     * 1.阻塞序列，线程停下来不干任何事
     */
    @Test
    fun sequence() {
        sequence.forEach { printInfo("" + it) }
        //输出————————
        //1 ,10:08:54:981
        //2 ,10:08:56:06
        //3 ,10:08:57:11
        //BUILD SUCCESSFUL in 3s
    }


     private fun createFlow() = flow {
        for (i in 1..3) {
            delay(1000)
            //发送信号
            emit(i)
        }
    }

    /**
     * 2.flow 使用，异步发送消息
     */
    @Test
    fun emitData() = runBlocking {
        launch {
            for (i in 1..10){
                delay(600)
                //flow发送信号是不阻塞线程的
                printInfo("我也在运行 $i")
            }
        }

        createFlow().collect{
            printInfo("$it")
        }

        //输出————————可以看到，没有阻塞
        //我也在运行 ,10:25:35:457
        //1 ,10:25:35:857
        //我也在运行 ,10:25:36:78
        //我也在运行 ,10:25:36:681
        //2 ,10:25:36:860
        //我也在运行 ,10:25:37:287
        //3 ,10:25:37:864
    }

    /**
     * 3.flow 流的连接符 💜
     * collectLast 只取最后一个
     * take 取前面限制个数
     * map  获取前面的数据继续传递，可以转换类型，但仅限一个
     * transform  获取前一个数据，发送无数个emit，转换类型给后面的操作
     * reduce  累加，可以收集前一个值
     * zip  合并，即使异步也会保证最后一个处理完毕后再依次收集
     * flownOn 修改在此之上的线程，在生产者中
     * launchIn 修改在此之上的线程，在消费者中
     */
    @Test
    fun continueFlow() = runBlocking {
        (1..8).asFlow()
            .filter {
                delay(1000)
                printInfo("Flow started ${Thread.currentThread().name}")
                it % 2 == 0
            }
            .flowOn(Dispatchers.IO)
            //flowOn 切换线程，只作用于它之上的，之下的不管
            .map {
                printInfo("Flow started ${Thread.currentThread().name}")
                "过滤后 = $it"
            }
            .collect {
                printInfo("Flow started ${Thread.currentThread().name}")
                printInfo(it)
            }

        //输出——————
        //Flow started DefaultDispatcher-worker-1 @coroutine#2 ,10:49:10:41
        //Flow started DefaultDispatcher-worker-1 @coroutine#2 ,10:49:11:69
        //Flow started Test worker @coroutine#1 ,10:49:11:73
        //Flow started Test worker @coroutine#1 ,10:49:11:78
        //过滤后 = 2 ,10:49:11:78
        //Flow started DefaultDispatcher-worker-1 @coroutine#2 ,10:49:12:77
        //Flow started DefaultDispatcher-worker-1 @coroutine#2 ,10:49:13:80
        //Flow started Test worker @coroutine#1 ,10:49:13:83
        //Flow started Test worker @coroutine#1 ,10:49:13:83
        //过滤后 = 4 ,10:49:13:84

    }

    /**
     * launchOn切换接收函数的线程
     */
    @Test
    fun continueFlow2() = runBlocking {
        //这里需要 val job 才能避免编译器警告，不然会认为丢弃了这个协程不处理
        val job = (1..8).asFlow()
            .filter {
                delay(1000)
                printInfo("filter thread: ${Thread.currentThread().name}")
                it % 2 == 0
            }
            .flowOn(Dispatchers.IO)
            .map {
                printInfo("map thread: ${Thread.currentThread().name}")
                "过滤后 = $it"
            }
            .onEach {
                printInfo("onEach thread: ${Thread.currentThread().name}")
                printInfo(it)
            }
            .launchIn(CoroutineScope(Dispatchers.Default))

        //由于 launchIn 是在后台线程，非主线程，所以不会等它完成，会直接结束方法
        //要让此job等待完成才能看到输出
        //如果要切换在主线程执行任务，那么就用collect()

        //delay(10000) // 给 flow 执行时间
        //job.join()

        //输出————
        //filter thread: DefaultDispatcher-worker-2 @coroutine#3 ,11:01:41:475
        //filter thread: DefaultDispatcher-worker-2 @coroutine#3 ,11:01:42:502
        //map thread: DefaultDispatcher-worker-1 @coroutine#2 ,11:01:42:508
        //onEach thread: DefaultDispatcher-worker-1 @coroutine#2 ,11:01:42:515
        //过滤后 = 2 ,11:01:42:516
        //filter thread: DefaultDispatcher-worker-1 @coroutine#3 ,11:01:43:509
        //filter thread: DefaultDispatcher-worker-1 @coroutine#3 ,11:01:44:511
        //map thread: DefaultDispatcher-worker-2 @coroutine#2 ,11:01:44:512
        //onEach thread: DefaultDispatcher-worker-2 @coroutine#2 ,11:01:44:512
        //过滤后 = 4 ,11:01:44:513
    }



    /**
     * 4.flow ‼️背压：生产者速度大于消费者
     *  🌟buffer 关键字
     *   不缓存：生产者100ms + 消费者100ms = 400ms 一次，循环3次 1.2s
     *   缓存：(1)生产者发送100ms时，此时消费者接收耗时300ms = 100+300
     *        (2)在这过程生产者以每100ms的速度持续发送，不等待消费者处理
     *        (3)在300ms内生产者发送完毕，放进缓存，此时消费者第一次也处理完了
     *        (4)此时只剩消费者专注处理剩余两次 600ms,所以 100 + 300 + 600 = 1s
     *  🌟conflate 关键字
     *  生产者与消费者并发进行，每次消费只处理当前最后接收一个的值
     */
    @Test
    fun pressFlow() = runBlocking {
        val time = measureTime{
            flow{
                for (i in 1..8){
                    delay(100)
                    emit(i)
                    printInfo("emit : $i")
                }
            }
                //不缓存：总时间 = 1.2s
                //缓存：总时间 = 1.0s
                //.buffer(50)
                .conflate()
                .collect{
                    delay(200)
                    printInfo("receive:$it")
                }
        }

        printInfo("总时间 = $time")
    }

}