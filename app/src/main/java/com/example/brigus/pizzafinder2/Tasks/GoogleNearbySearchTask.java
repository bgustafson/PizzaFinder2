package com.example.brigus.pizzafinder2.Tasks;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.utils.AppClass;
import com.example.brigus.pizzafinder2.utils.AsyncResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.brigus.pizzafinder2.fragments.SettingsFragment.DEFAULT_RADIUS;
import static com.example.brigus.pizzafinder2.fragments.SettingsFragment.DEFAULT_RADIUS_KEY;
import static com.example.brigus.pizzafinder2.fragments.SettingsFragment.SHARED_PREFS_KEY;


public class GoogleNearbySearchTask extends AsyncTask<Location, Integer, ArrayList<PizzaLocation>> {

    private AsyncResponse delegate = null;
    private ArrayList<PizzaLocation> locations;

    public GoogleNearbySearchTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    protected ArrayList<PizzaLocation> doInBackground(Location... location) {
        locations = new ArrayList<>();
        location[0].getLatitude();
        location[0].getLongitude();
        HttpClient httpclient = new DefaultHttpClient();

        try {

            SharedPreferences preferences = AppClass.getInstance().getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            String searchRadius = preferences.getString(DEFAULT_RADIUS_KEY, DEFAULT_RADIUS);

            String placeSearchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + location[0].getLatitude()
                    + ","
                    + location[0].getLongitude()
                    + "&radius="
                    + searchRadius
                    + "&types=food&name=pizza&sensor=true&key=AIzaSyDhIhAAThws366XlCyqtIAK-SfBGsctlJg";
            HttpGet placesGet = new HttpGet(placeSearchURL);

            HttpResponse response = httpclient.execute(placesGet);
            int responseCode = response.getStatusLine().getStatusCode();
            switch(responseCode) {
                case 200:
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        String val = EntityUtils.toString(entity);
                        JSONObject jObject = new JSONObject(val);
                        JSONArray jArray = jObject.getJSONArray("results");

                        int i = 0;
                        while(i < jArray.length()) {
                            JSONObject o = jArray.getJSONObject(i);

                            JSONObject obj = o.getJSONObject("geometry").getJSONObject("location");
                            double lat = obj.getDouble("lat");
                            double lng = obj.getDouble("lng");

                            //Need to build the location
                            Location curLocation = new Location("APP");
                            curLocation.setLatitude(lat);
                            curLocation.setLongitude(lng);

                            locations.add(new PizzaLocation(o.getString("name"), o.getString("vicinity"), o.getString("rating"), o.getString("price_level"), o.getString("place_id"), curLocation));
                            i++;
                        }
                    }
                    break;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return locations;
    }



    protected void onPostExecute(ArrayList<PizzaLocation> result) {
        delegate.processFinish(result);
    }
}
