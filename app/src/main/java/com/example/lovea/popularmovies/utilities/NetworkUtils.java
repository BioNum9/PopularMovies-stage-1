package com.example.lovea.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.lovea.popularmovies.data.DefaultValue;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lovea on 2017/4/4.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DATA_SOURCE =
            "http://api.themoviedb.org/3/movie/popular?";
    private static final String format = "json";

    private static final String MOVIE_PIC_BASE_URL =
            "http://image.tmdb.org/t/p/";
    private static final String DEVICE_SIZE =
            "w185";

    final static String QUERY_PARAM = "q";
    final static String SORT_PRAM = "sort_by";
    final static String API_KEY = "api_key";

    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

    public static URL buildUrl(String[] data){
        Uri builtUri = Uri.parse(MOVIE_DATA_SOURCE).buildUpon()
                .appendQueryParameter(SORT_PRAM,data[0])
                .appendQueryParameter(API_KEY, DefaultValue.MOVIE_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }



}
