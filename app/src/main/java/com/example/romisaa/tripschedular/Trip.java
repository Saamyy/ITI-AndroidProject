package com.example.romisaa.tripschedular;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Samy-WorkStation on 3/14/2017.
 */

public class Trip implements Parcelable{

    public static final String STATUS_DONE = "done";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_POSTPONED = "postponed";
    public static final String STATUS_UPCOMING = "upcoming";

    int id;
    String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    String name;
    String status;
    String aveSpeeed;
    String source;
    String destination;
    Long date;
    Long duration;
    //Notes []notes;
    ArrayList <Notes> notes;

    public Trip() {
    }



    protected Trip(Parcel in) {
        id = in.readInt();
        name = in.readString();
        status = in.readString();
        aveSpeeed = in.readString();
        source = in.readString();
        destination = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public ArrayList<Notes> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
    }

    public Trip(int id, String name, String status, String aveSpeeed, String source, String destination, Long date, Long duration, ArrayList<Notes> notes) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.aveSpeeed = aveSpeeed;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.duration = duration;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(aveSpeeed);
        dest.writeString(source);
        dest.writeString(destination);
    }
}
