package com.serien.android.androidserienprojekt.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.serien.android.androidserienprojekt.domain.SeriesItem;

import java.util.ArrayList;

/**
 * Created by Igor on 09.08.2015.
 */
public class SeriesRepository {

    private static final String DATABASE_NAME = "serieslist4.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "seriesItems";
    public static final String KEY_NAME = "name";
    public static final String KEY_YEAR = "year";
    public static final String KEY_ACTORS = "actors";
    public static final String KEY_RATING = "rating";
    public static final String KEY_PLOT = "plot";
    public static final String KEY_IMGPATH = "path";
    public static final String KEY_IMDBID = "id";
    public static final String KEY_WATCHED = "watched";
    public static final String KEY_IMAGE = "image";


    private SeriesDBOpenHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList allSeriesItemsName;


    public SeriesRepository(Context context){
        dbHelper = new SeriesDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public long addSeriesItem(SeriesItem seriesItem) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, seriesItem.getName());
        values.put(KEY_YEAR, seriesItem.getYear());
        values.put(KEY_ACTORS, seriesItem.getActors());
        values.put(KEY_RATING, seriesItem.getRating());
        values.put(KEY_PLOT, seriesItem.getPlot());
        values.put(KEY_IMGPATH, seriesItem.getImgPath());
        values.put(KEY_IMDBID, seriesItem.getImdbID());
        values.put(KEY_WATCHED, seriesItem.getWatched());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public boolean updateImage(String name, String seriesImage) {
        String sql="update "+DATABASE_TABLE+" set image='"+seriesImage+"' where "+KEY_NAME+" like ?";
        Object[] bindArgs={name};
        try{
            db.execSQL(sql, bindArgs);
            return true;
        }catch(SQLException ex){
            return false;
        }
    }

    public boolean updateWatchedEpisodes(String name, String watchedEpisodes) {
        String sql="update "+DATABASE_TABLE+" set watched='"+watchedEpisodes+"' where "+KEY_NAME+" like ?";
        Object[] bindArgs={name};
        try{
            db.execSQL(sql, bindArgs);
            return true;
        }catch(SQLException ex){
            return false;
        }
    }

    public ArrayList<SeriesItem> getAllSeriesItems() {
        ArrayList<SeriesItem> allSeriesItems = new ArrayList<>();
        Cursor dBCursor = db.query(DATABASE_TABLE, new String[]{KEY_NAME, KEY_YEAR, KEY_ACTORS,
                KEY_RATING, KEY_PLOT, KEY_IMGPATH, KEY_IMDBID, KEY_WATCHED, KEY_IMAGE}, null, null, null, null, null);

        if(dBCursor.moveToFirst()){
            do{
                SeriesItem series = new SeriesItem(dBCursor.getString(0), dBCursor.getString(1), dBCursor.getString(2),
                        dBCursor.getString(3), dBCursor.getString(4), dBCursor.getString(5), dBCursor.getString(6), dBCursor.getString(7), dBCursor.getString(8));
                allSeriesItems.add(series);
            }while(dBCursor.moveToNext());
        }
        return allSeriesItems;
    }

    public String getImage(String seriesName) {
        String imageString = null;
        Cursor dBCursor = db.query(DATABASE_TABLE, new String[]{KEY_IMAGE}, KEY_NAME+"='"+seriesName+"'", null, null, null, null);
        if(dBCursor.moveToFirst()){
            do{
                imageString = dBCursor.getString(0);
            }while(dBCursor.moveToNext());
        }
        return imageString;
    }

    public long deleteSeries(String name) {
        return db.delete(DATABASE_TABLE, KEY_NAME + " =?", new String[]{name});
    }

    public SeriesItem getSeriesItem(String name) {
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_NAME, KEY_YEAR, KEY_ACTORS,
                KEY_RATING, KEY_PLOT, KEY_IMGPATH, KEY_IMDBID, KEY_WATCHED, KEY_IMAGE}, KEY_NAME + "=?", new String[]{name}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return new SeriesItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        } else {
            return null;
        }
    }

    public ArrayList<String> getAllSeriesNames() {
        ArrayList<String> allSeriesName = new ArrayList<>();
        Cursor dBCursor = db.query(DATABASE_TABLE, new String[]{KEY_NAME}, null, null, null, null, null);
        if(dBCursor.moveToFirst()){
            do{
                allSeriesName.add((dBCursor.getString(0)));
            }while(dBCursor.moveToNext());
        }
        return allSeriesName;
    }


    private class SeriesDBOpenHelper extends SQLiteOpenHelper{
        private static final String DATABASE_CREATE = "CREATE TABLE "
                + DATABASE_TABLE + "(" + KEY_NAME + " TEXT PRIMARY KEY,"
                + KEY_YEAR + " TEXT,"
                + KEY_ACTORS + " TEXT,"
                + KEY_RATING + " TEXT,"
                + KEY_PLOT + " TEXT,"
                + KEY_IMGPATH + " TEXT,"
                + KEY_IMDBID + " TEXT,"
                + KEY_WATCHED + " TEXT,"
                + KEY_IMAGE + " TEXT" + ")";

        public SeriesDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
            onCreate(db);

        }
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

}
