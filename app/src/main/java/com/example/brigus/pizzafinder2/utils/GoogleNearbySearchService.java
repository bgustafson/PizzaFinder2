package com.example.brigus.pizzafinder2.utils;


import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.Model.Results;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface GoogleNearbySearchService {

    @GET("place/nearbysearch/json")
    Call<Results> listLocations(@Query("location") String location,
                                @Query("radius") String radius,
                                @Query("types") String types,
                                @Query("name") String name,
                                @Query("sensor") String sensor,
                                @Query("key") String key);
}

