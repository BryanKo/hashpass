package com.example.david.testing;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class foodCurrent extends AppCompatActivity {
    YelpFusionApiFactory apiFactory;
    TextView businessName, businessLoc, businessRating, businessPrice, businessDist, businessWeather;
    String appId = "3v_MqsnS4xUByPuMTTjKZw";
    String appSecret = "41AchC7qNowuPe2y2GPUnGPj4Xc25h9SRCEyuSzU7QYZKq6gzfgTUyyHpu69PohB";
    ArrayList<Business> businesses;
    int businessIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // StrictMode allows the api to load on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_current);


        businessName = (TextView) findViewById(R.id.tvFoodName);
        businessLoc = (TextView) findViewById(R.id.tvFoodLoc);
        businessRating = (TextView) findViewById(R.id.tvFoodRating);
        businessPrice = (TextView) findViewById(R.id.tvFoodPrice);
        businessDist = (TextView) findViewById(R.id.tvFoodDist);
        businessWeather = (TextView) findViewById(R.id.tvWeather);

        Bundle extras = getIntent().getExtras();
        String[] currWeather = extras.getStringArray("passCurrWeather");
        String[] currLoc = extras.getStringArray("passCurrLoc");
        System.out.println(currWeather[0]);
        businessWeather.setText(currWeather[0]);

        apiFactory = new YelpFusionApiFactory();
        try {
            // Api call with client id and client secret id
            YelpFusionApi yelpFusionApi = apiFactory.createAPI(appId, appSecret);

            // Hashmap to store parameters
            Map<String, String> params = new HashMap<>();
            params.put("term", "chinese food");
            //params.put("radius", "5");
            params.put("location", "santa cruz");
            params.put("open_now", "true");

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            SearchResponse searchResponse = call.execute().body();

            businesses = searchResponse.getBusinesses();

            Business business = businesses.get(businessIndex);

            businessName.setText(business.getName());
            businessLoc.setText(business.getLocation().getAddress1() + ", " + business.getLocation().getCity() + ", " + business.getLocation().getState() + " " + business.getLocation().getZipCode());
            businessRating.setText(String.valueOf(business.getRating()));
            businessPrice.setText(business.getPrice());
            businessDist.setText(String.valueOf(round((business.getDistance() / 1609.34), 2)).concat(" miles"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
