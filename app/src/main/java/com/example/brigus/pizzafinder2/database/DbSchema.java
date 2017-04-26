package com.example.brigus.pizzafinder2.database;


import android.content.ContentValues;
import com.example.brigus.pizzafinder2.model.PizzaLocation;

public class DbSchema {

    protected static final String CREATE_PIZZA_LOCATIONS_TABLE = "create table " + DbSchema.PizzaFavoriteTable.NAME + " (" +
            "_id integer primary key autoincrement, " +
            DbSchema.PizzaFavoriteTable.Cols.ID + ", " +
            DbSchema.PizzaFavoriteTable.Cols.NAME + ", " +
            DbSchema.PizzaFavoriteTable.Cols.ADDRESS + ", " +
            DbSchema.PizzaFavoriteTable.Cols.RATING + ", " +
            DbSchema.PizzaFavoriteTable.Cols.PRICE_LEVEL + ", " +
            DbSchema.PizzaFavoriteTable.Cols.ICON + ", " +
            DbSchema.PizzaFavoriteTable.Cols.LATITUDE + ", " +
            DbSchema.PizzaFavoriteTable.Cols.LONGITUDE + ")";

    public static final String HAS_ROW = "SELECT EXISTS(SELECT 1 FROM " + PizzaFavoriteTable.NAME + " WHERE " + PizzaFavoriteTable.Cols.ID + "=\"?\" LIMIT 1);";


    public static final class PizzaFavoriteTable {

        public static final String NAME = "PizzaLocations";

        public static final class Cols {
            public static String ID = "id";
            public static String NAME = "name";
            public static String ADDRESS = "address";
            public static String RATING = "rating";
            public static String PRICE_LEVEL = "price_level";
            public static String ICON = "icon";
            public static String LATITUDE = "lat";
            public static String LONGITUDE = "lng";
        }
    }

    public static ContentValues getContentValues(PizzaLocation pizzaLocation) {

        ContentValues values = new ContentValues();
        values.put(PizzaFavoriteTable.Cols.ID, pizzaLocation.getId());
        values.put(PizzaFavoriteTable.Cols.NAME, pizzaLocation.getName());
        values.put(PizzaFavoriteTable.Cols.ADDRESS, pizzaLocation.getAddress());
        values.put(PizzaFavoriteTable.Cols.RATING, pizzaLocation.getRating());
        values.put(PizzaFavoriteTable.Cols.PRICE_LEVEL, pizzaLocation.getPrice_level());
        values.put(PizzaFavoriteTable.Cols.ICON, pizzaLocation.getIcon());
        values.put(PizzaFavoriteTable.Cols.LATITUDE, String.valueOf(pizzaLocation.getGeometry().getLocation().getLat()));
        values.put(PizzaFavoriteTable.Cols.LONGITUDE, String.valueOf(pizzaLocation.getGeometry().getLocation().getLng()));

        return values;
    }
}
