package com.example.coroutine.jetpack.paging.net;

import retrofit2.http.GET;

public interface MovieService {

    @GET("/movie.do")
    void getMovieList(int from,int count);
}
