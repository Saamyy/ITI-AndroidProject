package com.example.romisaa.tripschedular;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Samy-WorkStation on 3/15/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tripManager";
    // trips table name
    private static final String TABLE_TRIPS = "trips";
    //trips table content
    private static final String TRIP_ID = "_id";
    private static final String NAME = "name";
    private static final String DURATION = "Duration";
    private static final String DATE = "date";
    private static final String STATUS = "status";
    private static final String AVE_SPEED = "aveSpeed";
    private static final String SOURCE = "source";
    private static final String DESTINATION = "destination";

    // notes table name
    private static final String TABLE_NOTES = "notes";
    //notes table content
    private static final String NOTE_ID = "_id";
    private static final String TRIP_ID_FOR = "tripId";
    private static final String CONTENT = "content";
    public  DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TRIPS_TABLE = "CREATE TABLE " + TABLE_TRIPS + "("
                + TRIP_ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT,"
                + DURATION + " LONG," +DATE+" LONG,"+STATUS+" TEXT,"+AVE_SPEED+" TEXT,"+
                SOURCE+" TEXT,"+DESTINATION+" TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_TRIPS_TABLE);
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +CONTENT+" TEXT,"+TRIP_ID_FOR+" INTEGER,"+ "FOREIGN KEY(" + TRIP_ID_FOR
                + ") REFERENCES "
                + TABLE_TRIPS +"("+TRIP_ID+")"+")";
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(sqLiteDatabase);
    }
}
