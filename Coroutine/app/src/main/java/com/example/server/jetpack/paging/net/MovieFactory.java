package com.example.server.jetpack.paging.net;

import android.content.Context;

import dagger.hilt.android.EntryPointAccessors;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieFactory {

    public static MovieService getMovieApi(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new MovieInterceptor())
                .build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl("http://sdd/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MovieService.class);
    }

    public static MovieService getKotlinApi(Context context){
        MovieEntryPoint movieEntryPoint = EntryPointAccessors.fromApplication(
                context.getApplicationContext(), MovieEntryPoint.class);
        Retrofit retrofit = movieEntryPoint.getRetrofit();
        return retrofit.create(MovieService.class);
    }
}
