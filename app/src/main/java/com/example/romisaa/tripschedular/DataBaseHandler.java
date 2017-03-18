package com.example.romisaa.tripschedular;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Samy-WorkStation on 3/15/2017.
 */

public class DataBaseHandler {
    DataBaseHelper dbHelper;
    Calendar calendar;
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

    public  DataBaseHandler(Context context)
    {

        dbHelper=new DataBaseHelper(context);
    }
    public  int getNextTripId()
    {
        int maxid=0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TABLE_TRIPS, new String [] {"MAX(_id)"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.getString(0)!=null) {
                maxid = Integer.parseInt(cursor.getString(0));
            }
        }
        return maxid+1;
    }


    public  void addTrip(Trip newTrip)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIP_ID,newTrip.getId());
        values.put(TRIP_ID,Calendar.getInstance().get(Calendar.MILLISECOND));
        values.put(NAME,newTrip.getName());
        values.put(DURATION,newTrip.getDuration());
        values.put(DATE,newTrip.getDate());
        values.put(STATUS,newTrip.getStatus());
        values.put(AVE_SPEED,newTrip.getAveSpeeed());
        values.put(SOURCE,newTrip.getSource());
        values.put(DESTINATION,newTrip.getDestination());
        if (newTrip.getNotes()!=null) {
            int counter = 0;

            System.out.println("lenth l array "+newTrip.getNotes().length);
            for (int i = 0; i < newTrip.getNotes().length; i++)
            {
                newTrip.getNotes()[counter].setTripId(newTrip.getId());
                addNote(newTrip.getNotes()[counter]);
                System.out.println(newTrip.getNotes()[counter].getContent());
                ++counter;

            }
        }
        db.insert(TABLE_TRIPS, null, values);

    }

    public  void  deleteTrip(int tripId)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        delteAllTripNotes(tripId);
        db.delete(TABLE_TRIPS, TRIP_ID + " = ?",
                new String[] { String.valueOf(tripId) });
        System.out.println("3adn l delte ");
    }

    public int updateTrip(Trip updatedtrip)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRIP_ID,updatedtrip.getId());
        values.put(NAME,updatedtrip.getName());
        values.put(DURATION,updatedtrip.getDuration());
        values.put(DATE,updatedtrip.getDate());
        values.put(STATUS,updatedtrip.getStatus());
        values.put(AVE_SPEED,updatedtrip.getAveSpeeed());
        values.put(SOURCE,updatedtrip.getSource());
        values.put(DESTINATION,updatedtrip.getDestination());

        // updating row
         return db.update(TABLE_TRIPS, values, TRIP_ID + " = ?",
                new String[] { String.valueOf(updatedtrip.getId()) });
    }
    public  int changeStatus(int tripId,String newStatus)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STATUS,newStatus);
        return db.update(TABLE_TRIPS, values, TRIP_ID + " = ?",
                new String[] { String.valueOf(tripId) });


    }
    public  int changeStartTime(int tripId,Long newStartTime)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE,newStartTime);
        return db.update(TABLE_TRIPS, values, TRIP_ID + " = ?",
                new String[] { String.valueOf(tripId) });

    }

    public  int changeSource(int tripId,String newSource)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SOURCE,newSource);
        return db.update(TABLE_TRIPS, values, TRIP_ID + " = ?",
                new String[] { String.valueOf(tripId) });
    }
    public  void addNote(Notes newNote)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIP_ID_FOR,newNote.getTripId());
        values.put(CONTENT,newNote.getContent());
        db.insert(TABLE_NOTES, null, values);


    }
    public  void delteNote(int noteID)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NOTES, NOTE_ID + " = ?",
                new String[] { String.valueOf(noteID) });
    }
    public  void delteAllTripNotes(int tripID)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NOTES, TRIP_ID_FOR + " = ?",
                new String[] { String.valueOf(tripID) });
    }

    public  int updateNote(Notes updatedNote)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTENT,updatedNote.getContent());
        // updating row
        return db.update(TABLE_TRIPS, values, TRIP_ID + " = ?",
                new String[] { String.valueOf(updatedNote.getNoteId()) });

    }
    public ArrayList<Trip> getAllUpcommingTrips()
    {
        ArrayList<Trip> triptList = new ArrayList<Trip>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Notes> tripNotes=new ArrayList<>();

        Cursor cursor = db.query(TABLE_TRIPS, new String[] { TRIP_ID,
                        NAME, DURATION,DATE, STATUS,AVE_SPEED,SOURCE,DESTINATION}, STATUS + "=?",
                new String[] { "upcoming" }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.setId(cursor.getInt(0));
                trip.setName(cursor.getString(1));
                trip.setDuration(cursor.getLong(2));
                trip.setDate(cursor.getLong(3));
                trip.setStatus(cursor.getString(4));
                trip.setAveSpeeed(cursor.getString(5));
                trip.setSource(cursor.getString(6));
                trip.setDestination(cursor.getString(7));
                tripNotes=getTripNotes(trip.getId());
                System.out.println("id of the trup"+trip.getId());
                trip.setNotes( tripNotes.toArray(new Notes[tripNotes.size()]));

                // Adding contact to list
                triptList.add(trip);
            } while (cursor.moveToNext());


        }

        return  triptList;
    }

   public  ArrayList<Trip>  getallHistoryTrips()
   {
       ArrayList<Trip> triptList = new ArrayList<Trip>();
       ArrayList<Notes> tripNotes=new ArrayList<>();

       SQLiteDatabase db = dbHelper.getReadableDatabase();

       Cursor cursor = db.query(TABLE_TRIPS, new String[] { TRIP_ID,
                       NAME, DURATION,DATE, STATUS,AVE_SPEED,SOURCE,DESTINATION}, STATUS + "!=?",
               new String[] { "upcoming" }, null, null, null, null);
       if (cursor.moveToFirst()) {
           do {
               Trip trip=new Trip();

               trip.setId(cursor.getInt(0));
               trip.setName(cursor.getString(1));
               trip.setDuration(cursor.getLong(2));
               trip.setDate(cursor.getLong(3));
               trip.setStatus(cursor.getString(4));
               trip.setAveSpeeed(cursor.getString(5));
               trip.setSource(cursor.getString(6));
               trip.setDestination(cursor.getString(7));
               tripNotes=getTripNotes(trip.getId());
               System.out.println("in history");
               System.out.println("id of the trup"+trip.getId());
               trip.setNotes( tripNotes.toArray(new Notes[tripNotes.size()]));
               // Adding contact to list
               System.out.println("after setnotes");
               triptList.add(trip);
           } while (cursor.moveToNext());

       }
       return  triptList;
   }

    public  ArrayList<Notes> getallNotes()
    {
        ArrayList<Notes> notestList = new ArrayList<Notes>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[] { NOTE_ID,
                        CONTENT, TRIP_ID_FOR}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Notes notes = new Notes();
                notes.setNoteId(Integer.parseInt(cursor.getString(0)));
                notes.setContent(cursor.getString(1));
                if(cursor.getString(2)!=null)

                notes.setTripId(Integer.parseInt(cursor.getString(2)));

                // Adding contact to list
                notestList.add(notes);
            } while (cursor.moveToNext());

        }
        return  notestList;
    }

    public  ArrayList<Notes> getTripNotes(int tripId)
    {
        ArrayList<Notes> notestList = new ArrayList<Notes>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[] { NOTE_ID,
                CONTENT, TRIP_ID_FOR}, TRIP_ID_FOR + "=?", new String [] {String .valueOf(tripId)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Notes notes = new Notes();
                notes.setNoteId(Integer.parseInt(cursor.getString(0)));
                notes.setContent(cursor.getString(1));
                if(cursor.getString(2)!=null)
                    notes.setTripId(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                notestList.add(notes);
            } while (cursor.moveToNext());

        }
        return  notestList;
    }
}

