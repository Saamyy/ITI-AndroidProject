package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.romisaa.tripschedular.R.id.dateValue;
import static com.example.romisaa.tripschedular.R.id.notesValue;
import static com.example.romisaa.tripschedular.R.id.sourceValue;
import static com.example.romisaa.tripschedular.R.id.statusValue;
import static com.example.romisaa.tripschedular.R.id.timeValue;

public class EditTrip extends AppCompatActivity {


    EditText name;
    EditText status;
    EditText date;
    EditText time;
    CheckBox tripKind;
    String strsource, strdestination;
    FloatingActionButton addNoteBtn;
    LinearLayout mLayout;
    ArrayList<EditText> notesEditTexts = new ArrayList<>();

    Trip newTrip;
    ArrayList<Notes> tripNotes;
    ArrayList<Notes> rtripNotes;

    Calendar calendar;
    int year, month, day, hours, minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        newTrip=(Trip) intent.getParcelableExtra("trip");
        System.out.println("l gy mn l edit"+newTrip.getStatus());


        //Filling Data
        calendar = Calendar.getInstance();
        String am_pm = ((calendar.get(Calendar.AM_PM)) == Calendar.AM) ? "am" : "pm";
        String myFormat = "MMM dd, yyyy";
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        //Referencing The Layout
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        addNoteBtn = (FloatingActionButton) findViewById(R.id.addNoteBtn);
        mLayout = (LinearLayout) findViewById(R.id.mLayout);
        name = (EditText) findViewById(R.id.name);

        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        tripKind = (CheckBox) findViewById(R.id.kind);


        name.setText(newTrip.getName());


        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.source);
        PlaceAutocompleteFragment placeAutocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination);
        strsource=newTrip.getSource();
        strdestination=newTrip.getDestination();
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                EditTrip.this.strsource=place.getName().toString();

            }

            @Override
            public void onError(Status status) {

            }
        });
        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                EditTrip.this.strdestination = place.getName().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });



    }

}
