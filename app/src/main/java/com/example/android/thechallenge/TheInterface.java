package com.example.android.thechallenge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheInterface
{
    //    Getting the movies review
    @GET("/users/{Uid}.json")
    Call<TheUser> getAllJournals(@Path("Uid") String Id);
}
