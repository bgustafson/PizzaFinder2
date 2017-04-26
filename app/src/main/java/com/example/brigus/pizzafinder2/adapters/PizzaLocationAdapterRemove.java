package com.example.brigus.pizzafinder2.adapters;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;
import com.example.brigus.pizzafinder2.database.DbSchema;
import com.example.brigus.pizzafinder2.database.PizzaDB;
import com.example.brigus.pizzafinder2.model.PizzaLocation;

import java.util.List;

public class PizzaLocationAdapterRemove extends PizzaLocationAdapterBase {

    public PizzaLocationAdapterRemove(Activity activity, List<PizzaLocation> data) {
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

                    PizzaLocation selectedFromList = mData.get(position);
                    final SQLiteDatabase db = new PizzaDB(mActivity.getApplicationContext()).getWritableDatabase();

                    db.delete(DbSchema.PizzaFavoriteTable.NAME, DbSchema.PizzaFavoriteTable.Cols.ID +" = ?", new String[] { selectedFromList.getId() });

                    //Remove swiped item from list and notify the RecyclerView
                    mAdapter.notifyItemRemoved(position);
                    mData.remove(position);
                    mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                }
            }
        };
    }
}
