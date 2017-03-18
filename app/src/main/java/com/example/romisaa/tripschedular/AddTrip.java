package com.example.romisaa.tripschedular;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class AddTrip extends AppCompatActivity {



    EditText name;

    EditText source;
    EditText destination;
    EditText note;
    EditText date;
    CheckBox tripKind;
    String strsource,strdestination;

    Trip newTrip;
    ArrayList <Notes> tripNotes;
    ArrayList <Notes> rtripNotes;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        name=(EditText) findViewById(R.id.name);
        note=(EditText) findViewById(R.id.note);
        date=(EditText) findViewById(R.id.date);
        tripKind=(CheckBox) findViewById(R.id.kind);
        PlaceAutocompleteFragment placeAutocompleteFragment= (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.source);
        PlaceAutocompleteFragment placeAutocompleteFragment2= (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                AddTrip.this.strsource=place.getName().toString();

            }

            @Override
            public void onError(Status status) {

            }
        });
        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                AddTrip.this.strdestination=place.getName().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });
        newTrip=new Trip();
        tripNotes=new ArrayList<>();
        rtripNotes=new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHandler handler=new DataBaseHandler(getApplicationContext());
                newTrip.setId((int) System.currentTimeMillis());
                newTrip.setName(String.valueOf(name.getText()));
                newTrip.setSource(strsource);
                newTrip.setDestination(strdestination);
                Notes in=new Notes();
                in.setContent(String.valueOf(note.getText()));
                tripNotes.add(in);
                newTrip.setNotes(tripNotes.toArray(new Notes[tripNotes.size()]));
                newTrip.setDate(null);
                newTrip.setStatus("upcoming");
                if(tripKind.isChecked()) {
                   // create new trip for round trip
                    Trip roundTrip=new Trip();
                    roundTrip.setName("Round trip of "+String.valueOf(name.getText()));
                    roundTrip.setSource(strdestination);
                    roundTrip.setDestination(strsource);
                    Notes ins=new Notes();
                    ins.setContent(String.valueOf(note.getText()));
                    rtripNotes.add(ins);
                    roundTrip.setNotes(rtripNotes.toArray(new Notes[rtripNotes.size()]));
                    roundTrip.setDate(null);
                    roundTrip.setStatus("upcoming");
                    handler.addTrip(roundTrip);
                }
                handler.addTrip(newTrip);
                Intent intent=new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}

