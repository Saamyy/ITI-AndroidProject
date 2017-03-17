package com.example.romisaa.tripschedular;

/**
 * Created by ehabm on 3/15/2017.
 */

public class Notes {
    int noteId;

    public Notes() {
    }

    public Notes(int noteId, String content) {

        this.noteId = noteId;
        this.content = content;
    }

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
}

