package com.example.brigus.pizzafinder2.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.brigus.pizzafinder2.PizzaDetailActivity;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.model.PizzaLocation;
import com.example.brigus.pizzafinder2.utils.AppUtilityFunctions;

import java.util.List;

public abstract class PizzaLocationAdapterBase extends RecyclerView.Adapter<PizzaLocationAdapterBase.PizzaViewHolder> {

    protected Activity mActivity;
    protected final List<PizzaLocation> mData;
    private final ItemTouchHelper mItemTouchHelper;
    protected final PizzaLocationAdapterBase mAdapter;

    abstract ItemTouchHelper.SimpleCallback buildTouchCallback();

    public PizzaLocationAdapterBase(Activity activity, List<PizzaLocation> data) {
        this.mData = data;
        this.mActivity = activity;
        mAdapter = this;
        mItemTouchHelper = new ItemTouchHelper(buildTouchCallback());
    }


    @Override
    public PizzaLocationAdapterBase.PizzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);

        return new PizzaLocationAdapterBase.PizzaViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PizzaLocationAdapterBase.PizzaViewHolder holder, int position) {

        PizzaLocation location = mData.get(position);
        holder.name.setText(location.getName());
        holder.address.setText(location.getAddress());
        holder.rating.setText(location.getRating());

        int price;
        try{
            price = Integer.parseInt(location.getPrice_level());
        } catch (NumberFormatException ex) {
            price = -1;
        }

        holder.price.setText(AppUtilityFunctions.convertPrice(price));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public ItemTouchHelper getItemTouchHelper() {
        return mItemTouchHelper;
    }


    public class PizzaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address) TextView address;
        @BindView(R.id.rating) TextView rating;
        @BindView(R.id.price) TextView price;

        public PizzaViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //view.setOnClickListener(this);
        }

        @Override
        @OnClick(R.id.item_container)
        public void onClick(View v) {

            PizzaLocation selectedFromList = mData.get(this.getPosition());

            //open the details view here
            Intent i = new Intent(mActivity, PizzaDetailActivity.class);
            i.putExtra("name", selectedFromList.getName());
            i.putExtra("location", selectedFromList);

            mActivity.startActivity(i);
        }
    }

}
