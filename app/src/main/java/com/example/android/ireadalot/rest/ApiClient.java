package com.example.android.ireadalot.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gjezzi on 14/07/16.
 */
public class ApiClient {

    //private static final String API_BASE_URL = "http://openlibrary.org/";
    private static final String API_BASE_URL = "https://www.googleapis.com/books/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
