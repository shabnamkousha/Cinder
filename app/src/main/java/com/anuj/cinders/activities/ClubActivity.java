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
import com.anuj.cinders.utils.FoursquareTokenStore;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClubActivity extends AppCompatActivity {

    @Bind(R.id.rvClubView)
    RecyclerView clubViewRecyclerView;

    LinearLayoutManager linearLayoutManager;

    List<Club> clubs;

    ClubHomeAdapter clubHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        ButterKnife.bind(this);

        setToolbar();

        setupTheTimelineAdapter();

        Log.i("INFO", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+ FoursquareTokenStore.get().getToken());



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

        clubs = new LinkedList<>();
        clubHomeAdapter = new ClubHomeAdapter(clubs);

        // give our custom adapter to the recycler view
        clubViewRecyclerView.setAdapter(clubHomeAdapter);
    }

}
