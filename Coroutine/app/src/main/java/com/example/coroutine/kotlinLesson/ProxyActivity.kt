package com.example.coroutine.kotlinLesson

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutine.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * 属性委托
 *
 * 只有写了 by 的变量定义，才会触发实现的委托逻辑
 * 所以不会影响 findViewById
 */
operator fun TextView.provideDelegate(value:Any?,property:KProperty<*>) =
    object : ReadWriteProperty<Any?,String?>{
        override fun getValue(thisRef: Any?, property: KProperty<*>): String {
            println("TextView重写get == $text ，${property.name}")
            /**
             * 可以看到在委托时正是把TextView转换为了String，所以才可以变成String的代理
             */
            return text as String
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            println("TextView== $text ，${property.name},value = $value")
            text = value
        }
    }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn1 = findViewById<Button>(R.id.button)
        val btn2 = findViewById<Button>(R.id.button2)
        val tv = findViewById<TextView>(R.id.textView)
        val tv2 = findViewById<TextView>(R.id.textView2)

        /**
         * by 关键字相当于写了：
         * val delegate = TextView()
         * var content1
         *     get() = delegate.getValue(this, ::name)
         *     set(value) = delegate.setValue(this, ::name, value)
         * 能够通过 by 关键字委托的，都需要实现 provideDelegate 方法
         */
        var content1 : String ? by tv
        var content2 : String ? by tv2

        btn1.setOnClickListener {
            content1 = "正在运行..."
            content2 = content1

            Thread.sleep(1000)

            content1 = "到10楼了"
            content2 = content1
        }

        btn2.setOnClickListener {
            content1 = "正在运行..."
            content2 = content1

            Thread.sleep(1000)

            content1 = "到1楼了"
            content2 = content1
        }
    }
}