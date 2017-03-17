package com.example.romisaa.tripschedular;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samy-WorkStation on 3/14/2017.
 */

public class Trip implements Parcelable{
    int id;
    String name;
    String status;
    String aveSpeeed;
    String source;
    String destination;
    Long date;
    Long duration;
    Notes []notes;

    public Trip() {
    }

    public Trip(String aveSpeeed, Long date, String destination, Long duration, int id, String name, Notes[] notes, String source, String status) {
        this.aveSpeeed = aveSpeeed;
        this.date = date;
        this.destination = destination;
        this.duration = duration;
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.source = source;
        this.status = status;
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
