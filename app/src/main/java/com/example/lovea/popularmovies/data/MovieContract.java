package com.example.lovea.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lovea on 2017/5/16.
 */

public class MovieContract {
    // Content Authority
    public static final String CONTENT_AUTHORITY = "com.example.lovea.popularmovies";
    //Base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Path to the table
    public static final String PATH_MOVIE = "movies";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "reviews";

    //Entry for the Movies
    public static final class MovieEntry implements BaseColumns {
        // Content URI for the MovieEntry
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        //CONTENT_LIST represense the list of items and CONTENT_ITEM for the single item
        public static final String CONTENT_LIST =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_MOVIE;
        public static final String CONTENT_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        //columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_FAVORITE = "favorite";

        //Build a Uri for addressing a movie
        public static Uri buildMovieWithPoster(String posterUrl) {
            return CONTENT_URI.buildUpon()
                    .appendPath(posterUrl.substring(1)) //remove the heading slash
                    .build();
        }

        //Build a Uri for a record of the table
        public static Uri buildMovieWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //Get String type post URL from Uri
        public static String getPosterUrlFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        //Parse the ID of a record
        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

    }

    //Entry for the trailer
    public static final class TrailerEntry implements BaseColumns{
        // Content URI for the TrailerEntry
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        //CONTENT_LIST represense the list of items and CONTENT_ITEM for the single item
        public static final String CONTENT_LIST =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_TRAILER;
        public static final String CONTENT_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_TRAILER;

        public static final String TABLE_NAME = "trailers";

        // columns
        public static final String COLUMN_TITLE = "title"; //trailer title
        public static final String COLUMN_YOUTUBE_KEY = "youtube_key";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        //Get the id from Uri
        public static long getMovieIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        //get trailer uri with id
        public static Uri buildTrailerUriWithId(long movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }
    }

    //Entry for Review
    public static final class ReviewEntry implements BaseColumns {
        // Content URI for the ReviewEntry
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_LIST =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "reviews";

        // columns
        public static final String COLUMN_AUTHOR = "author"; //trailer title
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildTrailerWithId(long insertedId) {
            return ContentUris.withAppendedId(CONTENT_URI, insertedId);
        }

        public static long getMovieIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

}
