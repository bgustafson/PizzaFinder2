package com.example.brigus.pizzafinder2.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.PizzaDetailActivity;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.utils.AppUtilityFunctions;

import java.util.List;


public class PizzaLocationAdapter extends RecyclerView.Adapter<PizzaLocationAdapter.PizzaViewHolder> {

    private final Activity mActivity;
    private final List<PizzaLocation> mData;

    public PizzaLocationAdapter(Activity activity, List<PizzaLocation> data) {
        this.mData = data;
        this.mActivity = activity;
    }


    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);

        return new PizzaViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PizzaViewHolder holder, int position) {

        PizzaLocation location = mData.get(position);
        holder.name.setText(location.getName());
        holder.address.setText(location.getAddress());
        holder.rating.setText(location.getRating());
        holder.price.setText(AppUtilityFunctions.convertPrice(Integer.parseInt(location.getPrice_level())));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class PizzaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        public TextView name, address, rating, price;

        public PizzaViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            rating = (TextView) view.findViewById(R.id.rating);
            price = (TextView) view.findViewById(R.id.price);
            view.setOnClickListener(this);
        }

        @Override
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
