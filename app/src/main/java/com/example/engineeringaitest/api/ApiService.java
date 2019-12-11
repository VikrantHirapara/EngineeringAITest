package com.example.engineeringaitest.api;

import com.example.engineeringaitest.model.ItemHits;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search_by_date")
    Call<ItemHits> itemHits(@Query("page") int page, @Query("tags") String story);
}
