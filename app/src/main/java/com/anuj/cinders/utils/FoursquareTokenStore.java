package com.anuj.cinders.utils;

/**
 * Created by anujacharya on 2/21/16.
 */
public class FoursquareTokenStore {
    private static FoursquareTokenStore sInstance;
    private String token;

    public static FoursquareTokenStore get() {
        if (sInstance == null) {
            sInstance = new FoursquareTokenStore();
        }

        return sInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
