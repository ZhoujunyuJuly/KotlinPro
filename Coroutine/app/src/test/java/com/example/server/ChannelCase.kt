package com.example.server

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

class ChannelCase {

    /**
     * 1.channel通道 并发安全队列
     */
    //@Test
    fun channelBase() = runBlocking {
        val channel = Channel<Int>()
        val producer = launch {
            var i = 0
            printInfo("开始发送====")
            while (true){
                delay(2000)
                printInfo("发送：$i")
                channel.send(i++)
            }
        }
        val consumer = launch {
            printInfo("开始接收====")
            //接收这里也要死循环，这个是两个不同的协程，不然就只会接收一次
            while (true){
                delay(3000)
                val receive = channel.receive()
                //channel可以是一个迭代器，循环取值
                //1.可以while
//                val iterator=  channel.iterator()
//                while (iterator.hasNext()){
//
//                }

                //2.可以for
//                for ( element in channel){
//
//                }
                printInfo("==》：$receive")
            }
        }

        joinAll(producer,consumer)

        //输出
        /**
         * 分析channel是一个缓存队列，默认缓存数为0，也就是消费一个才能继续拿在一个
         * 所以发送者要等待接受者完成才可以继续发送
         */

        //开始发送==== ,14:27:08:98
        //开始接收==== ,14:27:08:106
        //发送：0 ,14:27:10:121(距离开始发送经过2s)
        //==》：0 ,14:27:11:112(距离开始接收经过3s)
        //发送：1 ,14:27:13:114(完成接收后等待2s)
        //==》：1 ,14:27:14:118(距离上一次接收相差3s)
        //发送：2 ,14:27:16:122(完成上一次接收后等待2s)
        //==》：2 ,14:27:17:120(距离上一次接收相差3s)
    }

    /**
     * 2.produce 快速创建生产者
     */
    //@Test
    fun fastCreate() = runBlocking {
        //创建了一个匿名内部类，里面自动实现了一个生产者，在不断的发消息
        //生产者很轻量，比如它只需要发送几个数字，消费者需要根据这几个数字处理不同的逻辑

        //当我们重点在消费者如何处理消息时，只需要拿到这个 receiveChannel 遍历取出就可以

        //可以想像为生产者和消费者都是大卡车，如果我是生产者，那我是给消费者创建一个大卡车
        //取名为消费者大卡车，这样消费者就识别这个大卡车从里面拿出食物
        //提供给谁用的，就叫什么队列
        val receiveQueue : ReceiveChannel<Int> = produce {
            repeat(10){
                delay(200)
                send(it)
            }
        }

        val run = launch {
            for (receive in receiveQueue){
                printInfo("$receive")
            }
        }

    }

    /**
     * 3.actor 快速创建消费者
     */
    //@Test
    fun fastReceive() =  runBlocking {
        //创建了一个匿名内部类，快速实现了一个消费者，如何处理生产者的事件
        //比如简单的弹toast
        //当生产者完成数据时，只要调用这个对象 send 出去接口，就有统一接收的地方
        val sendQueue : SendChannel<Int> = actor {
            for ( element in channel){
                printInfo("接收到了====${element}")
            }
        }

        val producer = launch {
            for ( i in 1 .. 10){
                sendQueue.send(i)
            }
            sendQueue.close()
        }
    }

    /**
     * 4.select关键字，选择最快返回的
     */
    //@Test
    fun selectUse() = runBlocking {
        /**
         *  ‼️这里加缓存的意义
         *  对于无缓存的通道，会先挂起 send(1),直到有一个receive来接收它
         *  🧠不加缓存时程序一直不结束：
         *      （1）select的定义是依次对 channels 注册监听，只执行最先到的
         *      （2）当监听到最快到达的通道，就不会再接收了
         *      （3）由于缓存为0，剩余的生产者包含的 send() 一直在挂起状态，等到接收
         *      （4）导致程序无法结束
         *  🧠加缓存会结束：
         *      先把 send() 放进缓存，就不会一直挂起阻塞程序
         */
        val channels = listOf(Channel(1),Channel(1),Channel<Int>(1))
        val job1 = launch {
            delay(100)
            channels[0].send(100)
        }

        val job2 = launch {
            delay(50)
            channels[1].send(50)
        }

        val job3 = launch {
            delay(130)
            channels[1].send(130)
        }



        //从多个挂起点中，选一个最先准备好的来执行
        val result = select<Int?>{
            //注册每一个channel，并不是执行它们
            channels.forEach{ channel ->
                //只会执行最先到的消息
                channel.onReceive{
                    printInfo("接收 = $it")
                    it
                }
            }
        }

        printInfo("选择的结果：$result")
    }
}