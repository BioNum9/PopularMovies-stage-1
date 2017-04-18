package com.example.lovea.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovea.popularmovies.domain.MovieModel;
import com.example.lovea.popularmovies.utilities.NormalUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lovea on 2017/4/8.
 */

public class MovieDetailActivity extends AppCompatActivity{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private MovieModel movieModel;

    private ImageView mMoviePicImageView;
    private TextView  mMovieNameTextView;
    private TextView  mMovieRatingTextView;
    private TextView  mMovieReleaseDateTextView;
    private TextView  mMoviePlotTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //imageView shows movie pic
        mMoviePicImageView = (ImageView)findViewById(R.id.movie_pic_image_view);
        //textView shows movie name
        mMovieNameTextView = (TextView)findViewById(R.id.movie_name_text_view);
        //textView shows movie rating
        mMovieRatingTextView = (TextView)findViewById(R.id.movie_rating_text_view);
        //textView shows movie release date
        mMovieReleaseDateTextView = (TextView)findViewById(R.id.movie_detail_release_date_text_view);
        //textView shows movie plot synopsis
        mMoviePlotTextView = (TextView)findViewById(R.id.movie_detail_plot_synopsis_text_view);

        //Use Gson to get object from JSON data
        Gson gson = new Gson();
        movieModel = gson.fromJson(getIntent().getStringExtra("movie_detail_json"), MovieModel.class);
        //Use Picasso to load movie pic into mMoviePicImageView
        Picasso.with(getApplicationContext()).load(NormalUtil.getFullPicPath(movieModel.getPosterPath())).into(mMoviePicImageView);
        //Set movie name and release year
        mMovieNameTextView.setText(movieModel.getOriginalTitle() + "(" + getYearFromDate(getDateFromString(movieModel.getReleaseDate())) + ")");
        //Set movie rating
        mMovieRatingTextView.setText(String.format("%.1f", movieModel.getVoteAverage()) + " / 10");
        //Set formatted movie release Date
        mMovieReleaseDateTextView.setText("Release at :" + getFormattedReleaseDateString(movieModel.getReleaseDate()));
        mMoviePlotTextView.setText(movieModel.getOverview());
    }

    /**
     * Helper method to get Date from String date format specified by TMDB
     * @param dateString The TMDB date in String
     * @return The Date object
     */
    private Date getDateFromString(String dateString) {
        Log.d(TAG, "DateStr: " + dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Parse String to Date faild" + dateString);
        }
        return date;
    }

    /**
     * Helper method to get the year from TMDB date
     * @param date The date
     * @return The year in String
     */
    private String getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) + "";
    }

    /**
     * Helper method that returns a date string formatted in *Date* *Month* *Year*
     * @param releaseDateString The release date in TMDB String format
     * @return The formatted date
     */
    private String getFormattedReleaseDateString(String releaseDateString) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateFromString(releaseDateString));
        return "" + calendar.get(Calendar.DATE) + " " + getMonthString(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
    }

    private String getMonthString(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


}
