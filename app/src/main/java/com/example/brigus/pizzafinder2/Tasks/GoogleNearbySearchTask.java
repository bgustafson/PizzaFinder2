package com.example.brigus.pizzafinder2.Tasks;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.Model.Results;
import com.example.brigus.pizzafinder2.utils.AppClass;
import com.example.brigus.pizzafinder2.utils.AsyncResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.brigus.pizzafinder2.fragments.SettingsFragment.*;


public class GoogleNearbySearchTask extends AsyncTask<Location, Integer, Void> {//ArrayList<PizzaLocation>> {


    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static final String API_KEY = "AIzaSyDhIhAAThws366XlCyqtIAK-SfBGsctlJg";

    private AsyncResponse delegate = null;
    private ArrayList<PizzaLocation> locations;

    public GoogleNearbySearchTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public void setDelegate(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected Void doInBackground(final Location... location) {
        locations = new ArrayList<>();
        location[0].getLatitude();
        location[0].getLongitude();
        //HttpClient httpclient = new DefaultHttpClient();
        OkHttpClient client = new OkHttpClient();

        //try {

            SharedPreferences preferences = AppClass.getInstance().getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            String searchRadius = preferences.getString(DEFAULT_RADIUS_KEY, DEFAULT_RADIUS);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GoogleNearbySearchService service = retrofit.create(GoogleNearbySearchService.class);
            Call<Results> call = service.listLocations(location[0].getLatitude() + "," + location[0].getLongitude(),
                    searchRadius, "food", "pizza", "true", API_KEY);

            call.enqueue(new Callback<Results>() {
                @Override
                public void onResponse(Call<Results> call, Response<Results> response) {

                    // handle response here
                    Results body = response.body();
                    locations.addAll(body.getResults());
                    delegate.processFinish(locations);
                }

                @Override
                public void onFailure(Call<Results> call, Throwable throwable) {

                    delegate.processFinish(locations);
                }
            });


            /*String placeSearchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
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
        }*/
        //return locations;
        return null;
    }



    //protected void onPostExecute(ArrayList<PizzaLocation> result) {
        //delegate.processFinish(result);
    //}
}
