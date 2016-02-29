package com.anuj.cinders.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.anuj.cinders.R;
import com.anuj.cinders.adapters.ClubHomeAdapter;
import com.anuj.cinders.models.Item;
import com.anuj.cinders.models.Photos;
import com.anuj.cinders.models.Venue;
import com.anuj.cinders.network.FoursquareClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import cz.msebera.android.httpclient.Header;

public class ClubActivity extends AppCompatActivity {

    @Bind(R.id.rvClubView)
    RecyclerView clubViewRecyclerView;

    LinearLayoutManager linearLayoutManager;

    List<Venue> venues;

    ClubHomeAdapter clubHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        ButterKnife.bind(this);

        setToolbar();

        setupTheTimelineAdapter();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("cinders") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://cinders.herokuapp.com/parse/").build());

        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Log.i("INFO", "Parse login done");
                }
            }
        });

        ParseObject venue = new ParseObject("venue");
        getVenuesNearMe(venue);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_club, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar(){
        Toolbar twitterToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(twitterToolBar);
        twitterToolBar.setBackgroundColor(getResources().getColor(R.color.toolbar));
        twitterToolBar.setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void setupTheTimelineAdapter(){

        // set properties of recycler
        clubViewRecyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        clubViewRecyclerView.setLayoutManager(linearLayoutManager);

        venues = new LinkedList<>();
        clubHomeAdapter = new ClubHomeAdapter(venues);

        // give our custom adapter to the recycler view
        clubViewRecyclerView.setAdapter(clubHomeAdapter);
    }

    private void getVenuesNearMe( ParseObject venue ){
        FoursquareClient fourSquareClient = new FoursquareClient();

        fourSquareClient.venueSearch(new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e("ERROR", "res=" + res + " statusCode=" + statusCode + " message=" + t.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("INFO", "getVenuesNearMe= " + responseString);
                Gson gson = new GsonBuilder().create();

                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(responseString).getAsJsonObject();
                JsonObject response = obj.getAsJsonObject("response");

                JsonArray venuesJsonArray = response.getAsJsonArray("venues");

                Type venueType = new TypeToken<List<Venue>>() {}.getType();
                List<Venue> venueList =  gson.fromJson(venuesJsonArray, venueType);
                // Get the pictures and store it in parse mongodatabse

                for(Venue venue : venueList){
                    getPhotosOfVenue(venue);
                }
            }
        });
    }



    private void getPhotosOfVenue(final Venue venue){

        final FoursquareClient fourSquareClient = new FoursquareClient();

        fourSquareClient.getVenuePhotos(venue.getId(), new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("ERROR", "getPhotosOfVenue from 4square");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("INFO", "@@@@@@@@@@" + responseString);
                Gson gson = new GsonBuilder().create();

                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(responseString).getAsJsonObject();
                JsonObject response = obj.getAsJsonObject("response");

                JsonObject photosJsonObj = response.getAsJsonObject("photos");
                Type photosType = new TypeToken<Photos>() {}.getType();
                Photos photos =  gson.fromJson(photosJsonObj, photosType);

                // this will give you all the photos at the venue
                List<Item> items = photos.getItems();

                StringBuilder photoUrl = new StringBuilder();
                for(Item item : items){

                    if(item.getVisibility().equalsIgnoreCase("public")){
                        photoUrl.append(item.getPrefix());
                        photoUrl.append("100x100");
                        photoUrl.append(item.getSuffix());
                        venue.setPhoto(photoUrl.toString());
                        break;
                    }
                }
                Log.i("INFO", "Venue=" + venue);

                venues.add(venue);
                clubHomeAdapter.notifyDataSetChanged();

            }
        });

    }

}
