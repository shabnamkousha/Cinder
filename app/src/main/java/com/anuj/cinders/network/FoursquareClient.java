package com.anuj.cinders.network;

import android.util.Log;

import com.anuj.cinders.utils.FoursquareTokenStore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by anujacharya on 2/21/16.
 */
public class FoursquareClient {

    private static final String FOURSQUARE = "https://api.foursquare.com/v2/";
/*
https://api.foursquare.com/v2/venues/search?
ll=40.7,-74&oauth_token=UEDXY3GAAE13DLDNY1K35CJE4AK24MB5EB5KCXANMSN23PZP&v=20160221
 */
    public void venueSearch(TextHttpResponseHandler textHttpResponseHandler) {

        String venueSearch = FOURSQUARE+"venues/search";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("oauth_token", FoursquareTokenStore.get().getToken());
        requestParams.put("ll","37.788716, -122.409178");
        requestParams.put("v","20160221");
        requestParams.put("m","foursquare");


        try {

            client.get(venueSearch, requestParams, textHttpResponseHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
