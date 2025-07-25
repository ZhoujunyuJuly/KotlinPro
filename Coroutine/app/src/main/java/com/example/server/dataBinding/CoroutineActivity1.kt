package com.example.server.dataBinding

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.server.dataBinding.net.User
import com.example.server.dataBinding.net.provideUserServiceApi
import com.example.coroutine.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * 协程作用域
 *
 * GlobalScope:生命周期是进程process级别，Activity或Fragment销毁，但协程依然执行
 * MainScope:在Activity使用，可以在onDestroy()取消
 * viewModelScope:只能在ViewModel用，绑定ViewModel的生命周期
 * lifecycleScope:只能在Activity/Fragment使用，绑定生命周期
 */

class CoroutineActivity1 : AppCompatActivity() {
    private var netResponse:TextView ?= null
    private var clickBtn:Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_retrofit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        netResponse = findViewById<TextView>(R.id.net_response)
        clickBtn = findViewById<Button>(R.id.net_button).also {
            it.setOnClickListener {
                //useNormalRequest()
                globalCoroutines()
                //mainScope()
            }
        }

    }

    /**
     * 使用协程——全局作用域
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun globalCoroutines() {
        GlobalScope.launch(Dispatchers.Main) {
            /**
             * 🌟优化点：不需要在withContext中执行网络操作，retrofit会自动切换到IO线程
             */
            val result = provideUserServiceApi().getUser()
            showText(result)
        }
    }

    /**
     * 协程，主线程作用域
     */
    private fun mainScope(){
        val mainScope = MainScope()
        mainScope.launch {
            val result = provideUserServiceApi().getUser()
            showText(result)
        }
    }

    val showText = {it : User ->
        netResponse?.text = "id = ${it.id}, name = ${it.name}, email = ${it.email}"
    }


    /**
     * 异步线程
     */
    private fun useNormalRequest(){
        val context = this@CoroutineActivity1
        //匿名内部类写法
        object : AsyncTask<Void,Void, User>(){
            override fun doInBackground(vararg p0: Void?): User {
                return provideUserServiceApi().loadUser().execute().body()!!
            }

            override fun onPostExecute(result: User?) {
                netResponse?.text = "id = ${result?.id} ,name = ${result?.name},email = ${result?.email}"
            }
        }.execute()
    }
}