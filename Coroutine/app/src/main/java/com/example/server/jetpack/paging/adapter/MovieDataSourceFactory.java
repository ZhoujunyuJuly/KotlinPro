package com.example.server.jetpack.paging.adapter;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.example.server.jetpack.paging.Movie;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {
    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        return null;
    }
}
