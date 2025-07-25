package com.example.server.jetpack.databinding;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ImageBindAdapter {

    @BindingAdapter("NetWorkImage")
    /**
     * 这里要用static！！不然会闪退
     * 你的某个 @BindingAdapter 绑定适配器 不是 static 方法
     *  但你又没有设置 DataBindingComponent，导致在运行时无法找到实例来调用非静态方法，于是崩溃。
     */
    public static void setImage(ImageView icon,String url){
        Glide.with(icon.getContext()).load(url).into(icon);
    }


    /**
     * 可以传入多种类型，以数组形式
     * @param icon
     * @param url
     * @param defaultId
     */
    @BindingAdapter(value = {"NetWorkImage","DefaultImage"})
    public static void setImage(ImageView icon,String url,int defaultId){
        if(TextUtils.isEmpty(url)){
            icon.setImageResource(defaultId);
        }else {
            Glide.with(icon.getContext())
                    .load(url).into(icon);
        }
    }
}
