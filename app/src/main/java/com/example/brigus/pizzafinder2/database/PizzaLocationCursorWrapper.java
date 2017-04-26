package com.example.brigus.pizzafinder2.database;


import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.brigus.pizzafinder2.model.PizzaLocation;

public class PizzaLocationCursorWrapper extends CursorWrapper {

    public PizzaLocationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public PizzaLocation getPizzaLocation() {

        String id = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.ID));
        String name = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.NAME));
        String address = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.ADDRESS));
        String rating = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.RATING));
        String price_level = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.PRICE_LEVEL));
        String lat = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.LATITUDE));
        String lng = getString(getColumnIndex(DbSchema.PizzaFavoriteTable.Cols.LONGITUDE));


        PizzaLocation location = new PizzaLocation(name, address, rating, price_level, id, lat, lng);

        return location;
    }
}
