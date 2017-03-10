package com.example.brigus.pizzafinder2.utils;


public class AppUtilityFunctions {


    public static String convertPrice(int price) {

        switch (price) {
            case 0:
                return "Price: Free";
            case 1:
                return "Price: $";
            case 2:
                return "Price: $$";
            case 3:
                return "Price: $$$";
            case 4:
                return "Price: $$$$" ;
            default:
                return "Price: N/A";
        }
    }
}
