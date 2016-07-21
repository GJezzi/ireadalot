package com.example.android.ireadalot.rest;

import com.example.android.ireadalot.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gjezzi on 14/07/16.
 */
public interface ApiInterface {
    @GET("volumes")
    Call<BookResponse> getBooks(@Query("q") String query);

}
