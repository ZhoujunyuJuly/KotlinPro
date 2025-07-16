package com.example.coroutine.jetpack.paging.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ImageBindingAdapter {

    /**
     * 这里需要加上static！不然会报错
     * @param icon
     * @param url
     */
    @BindingAdapter("MovieUrl")
    public static void setMovieUrl(ImageView icon,String url){
        Glide.with(icon.getContext())
                .load(url).into(icon);
    }
}
