package com.example.coroutine.jetpack.paging.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coroutine.databinding.PagingAdapterItemBinding;
import com.example.coroutine.jetpack.paging.Movie;
import com.example.coroutine.jetpack.paging.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mDatas = new ArrayList<>();

    public MovieAdapter(List<Movie> datas) {
        mDatas = datas;
    }

    public void setData(List<Movie> datas){
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PagingAdapterItemBinding binding = PagingAdapterItemBinding
                .inflate(LayoutInflater.from(parent.getContext()),
                        parent,false);

        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mDatas.get(position);
        holder.binding.setMovie(movie);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        private PagingAdapterItemBinding binding;

        public MovieViewHolder(@NonNull PagingAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
