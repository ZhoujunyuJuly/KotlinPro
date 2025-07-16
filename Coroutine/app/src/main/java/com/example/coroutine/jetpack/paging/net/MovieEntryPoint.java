package com.example.coroutine.jetpack.paging.net;


import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@EntryPoint
@InstallIn(SingletonComponent.class)
public interface MovieEntryPoint {
    Retrofit getRetrofit();
}
