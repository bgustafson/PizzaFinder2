package com.example.brigus.pizzafinder2.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class PizzaLocation implements Parcelable {

    private String name;
    @SerializedName("vicinity")
    private String address;
    private String rating;
    private String price_level;
    @SerializedName("place_id")
    private String id;
    private String icon;
    private Geometry geometry;

    public PizzaLocation(String name, String address, String rating, String price_level, String id, String lat, String lng)
    {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.price_level = price_level;
        this.id = id;
        this.geometry = new Geometry();

        Location l = new Location();

        l.setLat(Double.parseDouble(lat));
        l.setLng(Double.parseDouble(lng));
        this.geometry.setLocation(l);
    }

    private PizzaLocation (Parcel in) {

        String[] data = new String[7];
        in.readStringArray(data);

        this.name = data[0];
        this.address = data[1];
        this.rating = data[2];
        this.price_level = data[3];
        this.id = data[4];
        this.geometry = new Geometry();
        Location l = new Location();

        l.setLat(Double.parseDouble(data[5]));
        l.setLng(Double.parseDouble(data[6]));
        this.geometry.setLocation(l);
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

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
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
                String.valueOf(this.geometry.location.getLat()),
                String.valueOf(this.geometry.location.getLng())
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


    public class Geometry {

        @SerializedName("location")
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }


    public class Location {
        double lat;
        double lng;

        public Location() {

        }

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
