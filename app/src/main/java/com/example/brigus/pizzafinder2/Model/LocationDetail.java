package com.example.brigus.pizzafinder2.Model;

import android.media.Image;
import java.util.ArrayList;

public class LocationDetail {

    private String name;
    private String address;//formatted_address
    private String rating;
    private String price_level;
    private String phone;//formatted_phone_number
    private String url;
    private ArrayList<Image> photos;//
    private ArrayList<Review> reviews;//reviews[], photos[]

    public LocationDetail(String name, String address, String rating, String price_level, String phone, String url, ArrayList<Image> photos, ArrayList<Review> reviews)
    {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.price_level = price_level;
        this.phone = phone;
        this.url = url;
        this.photos = photos;
        this.reviews = reviews;
        //formatted_address, formatted_phone_number, reviews[], photos[]
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Image> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Image> photos) {
        this.photos = photos;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
