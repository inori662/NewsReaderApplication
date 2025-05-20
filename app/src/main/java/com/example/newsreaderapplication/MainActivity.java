package com.example.newsreaderapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://gnews.io/api/v4/";
    private static final String API_KEY = "a48b7c17e7bd42de7657aa9e8af8a68f"; // 🔐 Replace with your NewsAPI key

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NewsApiService.class);

        fetchArticles();

        swipeRefreshLayout.setOnRefreshListener(this::fetchArticles);
    }

    private void fetchArticles() {
        swipeRefreshLayout.setRefreshing(true);
        Call<NewsResponse> call = apiService.getTopHeadlines(API_KEY, "en", "us");


        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body().articles);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, "Error fetching news", t);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
