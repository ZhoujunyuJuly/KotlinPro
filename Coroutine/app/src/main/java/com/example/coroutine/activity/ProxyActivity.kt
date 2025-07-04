package com.example.coroutine.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coroutine.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


operator fun TextView.provideDelegate(value:Any?,property:KProperty<*>) =
    object : ReadWriteProperty<Any?,String?>{
        override fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return text as String
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            text = value
        }
    }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn1 = findViewById<Button>(R.id.button)
        val btn2 = findViewById<Button>(R.id.button2)
        val tv = findViewById<TextView>(R.id.textView)
        val tv2 = findViewById<TextView>(R.id.textView2)

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