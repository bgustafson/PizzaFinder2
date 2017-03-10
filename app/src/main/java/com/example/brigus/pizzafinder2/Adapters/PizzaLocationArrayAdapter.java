package com.example.brigus.pizzafinder2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.utils.AppUtilityFunctions;


public class PizzaLocationArrayAdapter extends ArrayAdapter<PizzaLocation> {

    private final Context context;
    private final PizzaLocation[] data;

    public PizzaLocationArrayAdapter(Context context, PizzaLocation[] data) {
        super(context, R.layout.location_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.location_item, parent, false);

        TextView textViewName = (TextView)rowView.findViewById(R.id.name);
        textViewName.setText(data[position].getName());
        TextView textViewAddress = (TextView)rowView.findViewById(R.id.address);
        textViewAddress.setText(data[position].getAddress());
        TextView textViewRating = (TextView)rowView.findViewById(R.id.rating);
        textViewRating.setText(context.getString(R.string.ratingLBL) + data[position].getRating());

        TextView textViewPriceLevel = (TextView)rowView.findViewById(R.id.price);
        textViewPriceLevel.setText(AppUtilityFunctions.convertPrice(Integer.parseInt(data[position].getPrice_level())));

        return rowView;
    }
}
