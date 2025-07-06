package com.example.coroutine.realPro.init

import android.content.Context
import androidx.startup.Initializer
import com.example.coroutine.realPro.model.CommonConstant

class AppInitializer : Initializer<Unit> {
    /**
     * 这个方法会在 Application 的 onCreate 方法执行
     */
    override fun create(context: Context) {
        CommonConstant.init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}