package com.example.server.realPro.model

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object CommonConstant {
    const val TAG = "realPro"
    const val IMAGE_URL = "https://i-blog.csdnimg.cn/direct/957931656b964b8298da40eb5fd4c85b.png"
    lateinit var mContext:Context
    fun init(context: Context){
        mContext = context
    }
}