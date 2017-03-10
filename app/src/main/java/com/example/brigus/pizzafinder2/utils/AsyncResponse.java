package com.example.brigus.pizzafinder2.utils;

import com.example.brigus.pizzafinder2.Model.PizzaLocation;

import java.util.ArrayList;


public interface AsyncResponse {

    void processFinish(ArrayList<PizzaLocation> output);
}
