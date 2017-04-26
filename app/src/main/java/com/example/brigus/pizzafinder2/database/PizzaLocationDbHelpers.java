package com.example.brigus.pizzafinder2.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.brigus.pizzafinder2.model.PizzaLocation;

import java.util.ArrayList;
import java.util.List;

public class PizzaLocationDbHelpers {

    public static List<PizzaLocation> getLocations(SQLiteDatabase db) {

        List<PizzaLocation> locations = new ArrayList<>();
        PizzaLocationCursorWrapper cursor = queryCrimes(db, null, null);

        try {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                locations.add(cursor.getPizzaLocation());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return locations;
    }

    private static PizzaLocationCursorWrapper queryCrimes(SQLiteDatabase db, String whereClause, String[] whereArgs) {

        Cursor cursor = db.query(
                DbSchema.PizzaFavoriteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PizzaLocationCursorWrapper(cursor);
    }
}
