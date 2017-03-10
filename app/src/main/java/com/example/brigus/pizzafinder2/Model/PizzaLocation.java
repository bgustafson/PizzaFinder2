package com.example.brigus.pizzafinder2.Model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;


public class PizzaLocation implements Parcelable {

    private String name;
    private String address;
    private String rating;
    private String price_level;
    private String id;
    private String icon;
    private Location location;



    public PizzaLocation(String name, String address, String rating, String price_level, String id, Location location)
    {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.price_level = price_level;
        this.id = id;
        this.location = location;
    }

    private PizzaLocation (Parcel in) {

        String[] data = new String[7];
        in.readStringArray(data);

        this.name = data[0];
        this.address = data[1];
        this.rating = data[2];
        this.price_level = data[3];
        this.id = data[4];

        Location l = new Location("");

        l.setLatitude(Double.parseDouble(data[5]));
        l.setLongitude(Double.parseDouble(data[6]));
        this.location = l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice_level() {
        return price_level;
    }

    public void setPrice_level(String price_level) {
        this.price_level = price_level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeStringArray( new String[] {
                this.name,
                this.address,
                this.rating,
                this.price_level,
                this.id,
                String.valueOf(this.location.getLatitude()),
                String.valueOf(this.location.getLongitude())
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<PizzaLocation>() {
        public PizzaLocation createFromParcel(Parcel in) {
            return new PizzaLocation(in);
        }

        public PizzaLocation[] newArray(int size) {
            return new PizzaLocation[size];
        }
    };
}
