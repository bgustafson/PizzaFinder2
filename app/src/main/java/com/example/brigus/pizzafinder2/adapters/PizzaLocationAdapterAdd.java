package com.example.brigus.pizzafinder2.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;
import com.example.brigus.pizzafinder2.database.DbSchema;
import com.example.brigus.pizzafinder2.database.PizzaDB;
import com.example.brigus.pizzafinder2.model.PizzaLocation;

import java.util.List;


public class PizzaLocationAdapterAdd extends PizzaLocationAdapterBase {


    public PizzaLocationAdapterAdd(Activity activity, List<PizzaLocation> data) {
        super(activity, data);
    }


    @Override
    ItemTouchHelper.SimpleCallback buildTouchCallback() {

        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(mActivity, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();

                if(direction == ItemTouchHelper.LEFT) {
                    //Remove swiped item from list and notify the RecyclerView
                    mAdapter.notifyItemRemoved(position);
                    mData.remove(position);
                    mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                } else if(direction == ItemTouchHelper.RIGHT) {
                    //Remove swiped item from list and notify the RecyclerView and add to favorites
                    PizzaLocation selectedFromList = mData.get(position);
                    ContentValues vals = DbSchema.getContentValues(selectedFromList);
                    final SQLiteDatabase db = new PizzaDB(mActivity.getApplicationContext()).getWritableDatabase();

                    String query = "SELECT 1 FROM " + DbSchema.PizzaFavoriteTable.NAME + " WHERE " + DbSchema.PizzaFavoriteTable.Cols.ID + "='" + selectedFromList.getId() + "' LIMIT 1;";
                    boolean exists = false;
                    Cursor cursor = db.rawQuery(query, null);
                    if(cursor.getCount() > 0) {
                        exists = true;
                    }
                    cursor.close();

                    if(!exists) {
                        db.insert(DbSchema.PizzaFavoriteTable.NAME, null, vals);
                        mAdapter.notifyItemRemoved(position);
                        mData.remove(position);
                        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                    }
                }
            }
        };
    }
}
