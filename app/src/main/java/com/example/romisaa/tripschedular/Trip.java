package com.example.romisaa.tripschedular;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ehabm on 3/15/2017.
 */

public class Trip implements Parcelable{
    int id;
    String name;
    String status;
    String type;
    String aveSpeeed;
    String source;
    String destination;
    String start;
    String date;
    Notes []notes;

    public Trip() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Trip(int id, String name, String status, String type, String aveSpeeed, Notes[] notes, String date, String source, String start, String destination) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.aveSpeeed = aveSpeeed;
        this.notes = notes;
        this.date = date;
        this.source = source;
        this.start = start;
        this.destination = destination;
    }

    public Notes[] getNotes() {
        return notes;
    }

    public void setNotes(Notes[] notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAveSpeeed() {
        return aveSpeeed;
    }

    public void setAveSpeeed(String aveSpeeed) {
        this.aveSpeeed = aveSpeeed;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
