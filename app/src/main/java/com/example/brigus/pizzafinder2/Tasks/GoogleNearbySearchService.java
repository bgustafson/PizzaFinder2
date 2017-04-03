package com.example.brigus.pizzafinder2.Tasks;

import com.example.brigus.pizzafinder2.Model.Results;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleNearbySearchService {

    @GET("place/nearbysearch/json")
    Call<Results> listLocations(@Query("location") String location,
                                @Query("radius") String radius,
                                @Query("types") String types,
                                @Query("name") String name,
                                @Query("sensor") String sensor,
                                @Query("key") String key);
}

