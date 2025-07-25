package com.example.server.jetpack.paging.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import com.example.server.jetpack.paging.Movie;


import kotlin.coroutines.Continuation;

public class MovieDataSource extends PagingSource<Integer, Movie> {

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return 0;
    }

    @Nullable
    @Override
    public Object load(@NonNull LoadParams<Integer> loadParams, @NonNull Continuation<? super LoadResult<Integer, Movie>> continuation) {
        return null;
    }
}
