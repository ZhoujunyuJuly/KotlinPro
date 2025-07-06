package com.example.coroutine.realPro.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.coroutine.R

@BindingAdapter("bindingAvatar")
fun bindingAvatar(imageView:ImageView, url:String?){
    if( url.isNullOrEmpty()){
        imageView.setImageResource(R.mipmap.ic_launcher)
        return
    }
    imageView.load(url){
        crossfade(true) //淡入淡出
        placeholder(R.mipmap.ic_launcher)
    }
}