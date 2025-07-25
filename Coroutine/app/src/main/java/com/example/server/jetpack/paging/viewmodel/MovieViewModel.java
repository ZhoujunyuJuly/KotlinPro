package com.example.server.jetpack.paging.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.server.jetpack.paging.Movie;

import java.util.List;

public class MovieViewModel extends ViewModel {
    public LiveData<List<Movie>> moviePageList;

    public MovieViewModel() {
    }
}
