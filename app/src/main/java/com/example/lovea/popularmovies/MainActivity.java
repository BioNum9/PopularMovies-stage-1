package com.example.lovea.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lovea.popularmovies.data.DefaultValue;
import com.example.lovea.popularmovies.domain.MovieModel;
import com.example.lovea.popularmovies.domain.MovieModelList;
import com.example.lovea.popularmovies.utilities.NormalUtil;
import com.example.lovea.popularmovies.utilities.RecyclerItemClickListener;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar myLoadingIndicator;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter movieAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    private String[] sortTypes = {"Most Popular","Highest Score"};

    private List<MovieModel> movieModels;

    private String sortType = "Most Popular";

    //private String singleSortType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView)findViewById (R.id.movie_pic_recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new GridLayoutManager(this,2);
        myRecyclerView.setLayoutManager(myLayoutManager);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        myLoadingIndicator = (ProgressBar) findViewById(R.id.my_loading_indicator);

        movieModels = new ArrayList<MovieModel>();

        movieAdapter = new MovieAdapter(movieModels);

        myRecyclerView.setAdapter(movieAdapter);

        myRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent detailActivityLaunchIntent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                        Gson gson = new Gson();
                        String movieJson = gson.toJson(movieModels.get(position), MovieModel.class);
                        detailActivityLaunchIntent.putExtra("movie_detail_json", movieJson);
                        startActivity(detailActivityLaunchIntent);
                    }
                })
        );
        //init sort:popular
        new GetMovieInfoTask().execute(sortType);
    }
    //Override onCreateOptionsMenu to inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.app_bar);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,sortTypes);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // If different sort type is selected in the spinner, refresh view with new API call
                if(!((position == 0 && sortType.equals(sortTypes[position]))  || (position == 1 && sortType.equals(sortTypes[position])))) {
                    sortType = sortTypes[position];
                    movieAdapter = new MovieAdapter(new ArrayList<MovieModel>());
                    myRecyclerView.setAdapter(movieAdapter);

                    new GetMovieInfoTask().execute(sortType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                myLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private class GetMovieInfoTask extends AsyncTask<String, Integer, List<MovieModel>> {

        @Override
        protected void onPreExecute() {
            myLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {

            //check if device online
            if(!NormalUtil.isOnline(getApplicationContext())) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "No Internet Connection,Please Check", Toast.LENGTH_LONG).show();
                //Looper.loop();
                return movieModels;
            }

            String SORT_TYPE = (params[0].equals(sortTypes[0])) ? "popular" : "top_rated";
            //get Url by sort type;
            String url = NormalUtil.getUrlFromParam(SORT_TYPE);
            String jsonString = fetchFromUrl(url);
            parseResponse(jsonString);
            return movieModels;
        }

        @Override
        protected void onPostExecute(List<MovieModel> movies) {

            myLoadingIndicator.setVisibility(View.INVISIBLE);
            movieAdapter = new MovieAdapter(movies);
            myRecyclerView.setAdapter(movieAdapter);
            Log.d(TAG, "New adapter set for movies");
        }

        /**
         * Helper method that fetches data from the urlString
         * @param urlString The URL that the HTTP request has to be made
         * @return The response in String
         */
        private String fetchFromUrl(String urlString) {
            URL url = null;
            String response = "";
            HttpURLConnection connection = null;

            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if(url != null) {
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try {
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    response = IOUtils.toString(inputStream);
                    Log.v(TAG, "Data: " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        /**
         * Helper method that parses the input and stores the list of movies
         * @param response The String response received from the server
         */
        private void parseResponse(String response) {
            if(!NormalUtil.isOnline(getApplicationContext())) {
                return;
            }
            Gson gson = new Gson();
            MovieModelList movieModelList = gson.fromJson(response, MovieModelList.class);
            if(movieModelList != null) {
                movieModels = movieModelList.getMovies();
            }
        }
    }

}

