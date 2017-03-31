package com.example.brigus.pizzafinder2.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


    public class PizzaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        @BindView(R.id.name) TextView name;
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
