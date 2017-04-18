package com.example.lovea.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lovea.popularmovies.data.DefaultValue;

/**
 * Created by lovea on 2017/4/8.
 */

public class NormalUtil {
    //get the full path of pic
    public static String getFullPicPath(String picPath){
        return DefaultValue.MOVIE_PIC_URL + picPath;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    //receive param and return Url (String type)
    public static final String getUrlFromParam(String sortType){

        //API base url
        String baseUrl = DefaultValue.BASE_URL;
        //API key
        String apiKeyParam = "?api_key=" + DefaultValue.MOVIE_API_KEY;

        String url = baseUrl + sortType + apiKeyParam;

        return url;
    };

}
