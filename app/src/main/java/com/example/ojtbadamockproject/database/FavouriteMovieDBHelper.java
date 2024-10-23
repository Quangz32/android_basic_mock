package com.example.ojtbadamockproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ojtbadamockproject.entities.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouriteMovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MoviesDB";

    private static final String TABLE_MOVIES = "movies";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RATING = "rating";

    public FavouriteMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_ADULT + " INTEGER,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RATING + " REAL"
                + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_ADULT, movie.isAdult() ? 1 : 0);
        values.put(KEY_POSTER_PATH, movie.getPosterPath());
        values.put(KEY_RELEASE_DATE, movie.getReleaseDate());
        values.put(KEY_OVERVIEW, movie.getOverview());
        values.put(KEY_RATING, movie.getRating());

        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(0));
                movie.setTitle(cursor.getString(1));
                movie.setAdult(cursor.getInt(2) == 1);
                movie.setPosterPath(cursor.getString(3));
                movie.setReleaseDate(cursor.getString(4));
                movie.setOverview(cursor.getString(5));
                movie.setRating(cursor.getFloat(6));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movieList;
    }

    public void deleteMovie(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_ID + " = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }

    public Set<Integer> getMovieIdsSet() {
        Set<Integer> movieIds = new HashSet<>();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                movieIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return movieIds;
    }


    // Add other CRUD methods like updateMovie, deleteMovie based on your requirements
}

//public Set<Integer> getMovieIdsSet() {
//    Set<Integer> movieIds = new HashSet<>();
//    String selectQuery = "SELECT " + MovieContract.MovieEntry._ID + " FROM " + MovieContract.MovieEntry.TABLE_NAME;
//    SQLiteDatabase db = this.getReadableDatabase();
//    Cursor cursor = db.rawQuery(selectQuery, null);
//
//    if (cursor.moveToFirst()) {
//        do {
//            int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
//            if (idColumnIndex >=0){
//                int movieId = cursor.getInt(idColumnIndex);
//                movieIds.add(movieId);
//            }
//        } while (cursor.moveToNext());
//    }
//
//    cursor.close();
//    db.close();
//    return movieIds;
//}