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

    Trip newTrip;
    ArrayList <Notes> tripNotes;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        name=(EditText) findViewById(R.id.name);
        source=(EditText) findViewById(R.id.source);
        destination=(EditText) findViewById(R.id.destination);
        note=(EditText) findViewById(R.id.note);
        date=(EditText) findViewById(R.id.date);
        tripKind=(CheckBox) findViewById(R.id.kind);
        newTrip=new Trip();
        tripNotes=new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHandler handler=new DataBaseHandler(getApplicationContext());
                newTrip.setId((int) System.currentTimeMillis());
                newTrip.setName(String.valueOf(name.getText()));
                newTrip.setSource(String.valueOf(source.getText()));
                newTrip.setDestination(String.valueOf(destination.getText()));
                Notes in=new Notes();
                in.setContent(String.valueOf(note.getText()));
                tripNotes.add(in);
                newTrip.setNotes(tripNotes.toArray(new Notes[tripNotes.size()]));
                newTrip.setDate(Long.valueOf(String.valueOf(date.getText())));
                if(tripKind.isChecked()) {
                   // create new trip for round trip
                }
                else
                {
                  // create only one trip
                }
                newTrip.setStatus("upcoming");
                handler.addTrip(newTrip);

                Intent intent=new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);

            }
        });

    }


}

