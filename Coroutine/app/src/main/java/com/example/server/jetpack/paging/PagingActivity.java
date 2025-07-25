package com.example.server.jetpack.paging;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coroutine.R;
import com.example.server.jetpack.paging.adapter.MovieAdapter;
import com.example.server.jetpack.paging.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class PagingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paging);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.paging_recyclerview);
        MovieAdapter adapter = new MovieAdapter(new ArrayList<>());
        recyclerView.setAdapter(new MovieAdapter(new ArrayList<>()));

        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.moviePageList.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setData(movies);
            }
        });
    }
}