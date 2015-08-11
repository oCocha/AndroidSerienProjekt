package com.serien.android.androidserienprojekt.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.serien.android.androidserienprojekt.domain.SeriesItem;

import java.util.ArrayList;

/**
 * Created by Igor on 09.08.2015.
 */
public class SeriesRepository {

    private static final String DATABASE_NAME = "serieslist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "seriesItem";
    public static final String KEY_NAME = "name";
    public static final String KEY_SERIES = "serie";

    private SeriesDBOpenHelper dbHelper;
    private SQLiteDatabase db;
    public ArrayList<SeriesItem> allSeriesItems = new ArrayList<>();
    public ArrayList<String> allSeriesItemsName = new ArrayList<>();


    public SeriesRepository(Context context){
        dbHelper = new SeriesDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public long addSeriesItem(SeriesItem seriesItem) {
        allSeriesItems.add(seriesItem);
        allSeriesItemsName.add(seriesItem.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, seriesItem.getName());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public ArrayList<SeriesItem> getAllSeriesItems() {
        return allSeriesItems;
    }

    public boolean checkList(String seriesName) {
        return allSeriesItemsName.contains(seriesName);
    }

    private class SeriesDBOpenHelper extends SQLiteOpenHelper{
        private static final String DATABASE_CREATE = "CREATE TABLE "
                + DATABASE_TABLE + "(" + KEY_NAME + " TEXT PRIMARY KEY" + ")";

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
