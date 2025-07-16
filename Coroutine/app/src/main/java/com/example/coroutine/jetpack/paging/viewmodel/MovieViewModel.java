package com.example.coroutine.jetpack.paging.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.coroutine.jetpack.paging.Movie;

import java.util.List;

public class MovieViewModel extends ViewModel {
    public LiveData<List<Movie>> moviePageList;

    public MovieViewModel() {
    }
}
