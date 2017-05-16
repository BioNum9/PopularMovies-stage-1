package com.example.lovea.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lovea on 2017/5/16.
 */

public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    //define int value for movie
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_POSTER = 101;
    public static final int MOVIE_WITH_ID = 102;

    //define int value for trailer
    public static final int TRAILER = 200;
    public static final int TRAILER_WITH_MOVIE_ID = 201;

    //define int value for review
    public static final int REVIEW = 300;
    public static final int REVIEW_WITH_MOVIE_ID = 301;

    private static final UriMatcher sUriMatcher = createUriMatcher();

    private SQLiteOpenHelper mOpenHelper;


    public static UriMatcher createUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MovieContract.PATH_MOVIE,MOVIE);
        matcher.addURI(authority,MovieContract.PATH_MOVIE+"/#",MOVIE_WITH_ID);
        matcher.addURI(authority,MovieContract.PATH_MOVIE+"/*",MOVIE_WITH_POSTER);

        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/#", TRAILER_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match){
            case MOVIE:
                cursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_WITH_ID:
                long _id = MovieContract.MovieEntry.getIdFromUri(uri);
                cursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{_id+""},
                        null,
                        null,
                        sortOrder
                );
            case MOVIE_WITH_POSTER:
                String posterUrl = MovieContract.MovieEntry.getPosterUrlFromUri(uri);
                Log.d(LOG_TAG, "poster url: " + posterUrl);
                cursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_IMAGE_URL + " = ?",
                        new String[]{posterUrl},
                        null,
                        null,
                        sortOrder
                );
                break;
            // TODO: 2017/5/17 starts at here
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
