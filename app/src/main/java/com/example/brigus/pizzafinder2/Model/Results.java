package com.example.brigus.pizzafinder2.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Results {

    @SerializedName("results")
    List<PizzaLocation> results;

    public List<PizzaLocation> getResults() {
        return results;
    }
}
