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

    private static final String NIGHT_CLUB_CATEGORY_ID  ="4d4b7105d754a06376d81259";
    /*
https://api.foursquare.com/v2/venues/search?
ll=40.7,-74&oauth_token=UEDXY3GAAE13DLDNY1K35CJE4AK24MB5EB5KCXANMSN23PZP&v=20160221
 */
    public void venueSearch(TextHttpResponseHandler textHttpResponseHandler) {

        String venueSearch = FOURSQUARE+"venues/search";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("oauth_token", FoursquareTokenStore.get().getToken());
        requestParams.put("ll","37.784552, -122.411010");
        requestParams.put("categoryId", NIGHT_CLUB_CATEGORY_ID);
        requestParams.put("radius","100000");
        requestParams.put("intent","browse");

        requestParams.put("v","20160221");
        requestParams.put("m","foursquare");

        try {

            client.get(venueSearch, requestParams, textHttpResponseHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * https://api.foursquare.com/v2/venues/VENUE_ID/photos
     * @param textHttpResponseHandler
     */
    public void getVenuePhotos(String venueId, TextHttpResponseHandler textHttpResponseHandler){
        String venueSearch = FOURSQUARE+"venues/"+venueId+"/photos";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("oauth_token", FoursquareTokenStore.get().getToken());
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
