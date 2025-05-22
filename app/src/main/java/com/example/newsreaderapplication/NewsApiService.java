package com.example.newsreaderapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("token") String apiKey,
            @Query("lang") String lang,
            @Query("country") String country,
            @Query("max") int maxResults // 👈 Add this line
    );

}
