package com.example.server.mvvm.mvp;

import com.example.server.mvvm.mvp.bean.GoodBean;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodsService {

    @GET("")
    Flowable<List<GoodBean>> getGoods();
    @GET("")
    Flowable<List<GoodBean>> getGoodsDetail(@Query("id")int id);
}
