package com.example.romisaa.tripschedular;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samy-WorkStation on 3/14/2017.
 */

public class Notes implements Parcelable {
    int noteId;

    public Notes() {
    }

    public Notes(int noteId, String content) {

        this.noteId = noteId;
        this.content = content;
    }

    protected Notes(Parcel in) {
        noteId = in.readInt();
        tripId = in.readInt();
        content = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public void setTripId(int tripId) {

        this.tripId = tripId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getTripId() {
        return tripId;
    }

    public int getNoteId() {
        return noteId;
    }

    int tripId;
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(noteId);
        dest.writeInt(tripId);
        dest.writeString(content);
    }
}
