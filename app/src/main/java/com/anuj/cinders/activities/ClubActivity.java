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
import com.anuj.cinders.dao.Club;
import com.anuj.cinders.models.Venue;
import com.anuj.cinders.models.VenueResponse;
import com.anuj.cinders.network.FoursquareClient;
import com.anuj.cinders.utils.FoursquareTokenStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

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

        getVenuesNearMe();

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
        twitterToolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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

    private void getVenuesNearMe(){
        FoursquareClient fourSquareClient = new FoursquareClient();

        fourSquareClient.venueSearch(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e("ERROR", "res=" + res + " statusCode=" + statusCode + " message=" + t.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("INFO", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+responseString);
                Gson gson = new GsonBuilder().create();

                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(responseString).getAsJsonObject();
                VenueResponse venueResponse =  gson.fromJson(obj, VenueResponse.class);

                venues.addAll(venueResponse.getResponse().getVenues());
                clubHomeAdapter.notifyDataSetChanged();

            }
        });
    }

}
