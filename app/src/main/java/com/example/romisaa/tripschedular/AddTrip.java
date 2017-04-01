package com.example.romisaa.tripschedular;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.vision.text.Line;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class AddTrip extends AppCompatActivity {


    EditText name;

    EditText source;
    EditText destination;
    EditText note;
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
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        //Filling Data
        calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
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
//        note = (EditText) findViewById(R.id.note);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        tripKind = (CheckBox) findViewById(R.id.kind);

        date.setText(sdformat.format(calendar.getTime()));
//        time.setText(timeFormat.format(calendar.getTime()));
        String am_pm = ((calendar.get(Calendar.AM_PM)) == Calendar.AM) ? "am" : "pm";
//        time.setText(calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+ " "+am_pm);
        String minute = calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE):""+calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR)==0?12:calendar.get(Calendar.HOUR);
        time.setText(hour+":"+minute+ " "+am_pm);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog dialog = new DatePickerDialog(AddTrip.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String myFormat = "MMM dd, yyyy";
                            SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            date.setText(sdformat.format(calendar.getTime()));

                        }
                    }, year, month, day);
                    dialog.show();
                    date.clearFocus();
                }
            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog dialog = new TimePickerDialog(AddTrip.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                            time.setText(timeFormat.format(calendar.getTime()));
//                            Toast.makeText(AddTrip.this, calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, hours, minutes, false);

                    dialog.show();
                    time.clearFocus();
                }
            }
        });

        addNoteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                for (EditText editText : notesEditTexts) {
                    if (editText.getText().toString().trim().equals(""))
                        return;
                }
                {//Creating Edit Text
                    final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    EditText editText = new EditText(getApplicationContext());
                    editText.setLayoutParams(lparams);
                    editText.setSingleLine(false);  //TODO Check
                    editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    editText.setTextColor(0xff000000);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setHint("Insert your note here");
                    editText.setHintTextColor(Color.DKGRAY);
                    notesEditTexts.add(editText);
                    //Creating Button

                    //Create Horizontal View
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    mLayout.addView(editText);
                }
            }
        });

        final PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.source);
        final PlaceAutocompleteFragment placeAutocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination);

        ((LinearLayout) placeAutocompleteFragment.getView()).removeView(placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button));
        ((LinearLayout) placeAutocompleteFragment2.getView()).removeView(placeAutocompleteFragment2.getView().findViewById(R.id.place_autocomplete_search_button));

        placeAutocompleteFragment.setHint("Choose source");
        placeAutocompleteFragment2.setHint("Choose destination");

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                System.out.println("longitude and latitude : "+place.getLatLng().latitude+","+place.getLatLng().longitude);
                String str=place.getLatLng().latitude+","+place.getLatLng().longitude+"#"+place.getName();
                AddTrip.this.strsource = str;

            }

            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAutocompleteFragment.setText("");
                AddTrip.this.strsource = "";
            }
        });

        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                System.out.println("longitude and latitude : "+place.getLatLng().latitude+","+place.getLatLng().longitude);
                String str=place.getLatLng().latitude+","+place.getLatLng().longitude+"#"+place.getName();
                AddTrip.this.strdestination = str;
            }

            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragment2.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAutocompleteFragment2.setText("");
                AddTrip.this.strdestination = "";
            }
        });


        newTrip = new Trip();
        tripNotes = new ArrayList<>();
        rtripNotes = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (!validateTrip())
                    return;

                DataBaseHandler handler = new DataBaseHandler(getApplicationContext());

                newTrip.setId((int) System.currentTimeMillis());    // TODO: 19-Mar-17 Find another way for unique int id
                newTrip.setName(String.valueOf(name.getText()));
                newTrip.setSource(strsource);
                newTrip.setDestination(strdestination);
                //for testing onlyy
                newTrip.setDuration((long) 0);

//                in.setContent(String.valueOf(note.getText()));
                for (EditText editText : notesEditTexts) {
                    Notes note = new Notes();
                    if (editText != null && editText.getText() != null && !editText.getText().toString().trim().equals("")) {
                        note.setContent(editText.getText().toString());
                        tripNotes.add(note);
                    }
                }

                //  newTrip.setNotes(tripNotes.toArray(new Notes[tripNotes.size()]));
                newTrip.setNotes(tripNotes);
                newTrip.setDate(calendar.getTimeInMillis());
                newTrip.setStatus(Trip.STATUS_UPCOMING);
                if (tripKind.isChecked()) {
                    // create new trip for round trip
                    Calendar returnCalendar = Calendar.getInstance();
                    returnCalendar.setTimeInMillis(calendar.getTimeInMillis());
                    returnCalendar.set(Calendar.HOUR_OF_DAY, returnCalendar.get(Calendar.HOUR_OF_DAY) + 2);

                    Trip roundTrip = new Trip();
                    roundTrip.setId((int) System.currentTimeMillis() + 1);
                    roundTrip.setName(String.valueOf(name.getText()) + " - Return");
                    roundTrip.setSource(strdestination);
                    roundTrip.setDestination(strsource);
                    roundTrip.setDuration((long) 0);
                    Notes ins = new Notes();
                    ins.setContent("Return trip of " + name.getText());
                    rtripNotes.add(ins);
                    roundTrip.setNotes(tripNotes);
                    roundTrip.setDate(returnCalendar.getTimeInMillis());
                    roundTrip.setStatus("upcoming");
                    TaskManager.getInstance(getApplicationContext()).setTask(roundTrip);
                    handler.addTrip(roundTrip);
                }
                handler.addTrip(newTrip);
                TaskManager.getInstance(getApplicationContext()).setTask(newTrip);
//                finishAffinity();
//                Intent intent = new Intent(view.getContext(), MainActivity.class);
//                startActivity(intent);

                finish();
                Log.i("MyTag", String.valueOf(newTrip.getDate()));


                //TODO delete - Just for testing
                for (Notes note : newTrip.getNotes()) {
                    Log.i("MyTag", note.getContent());
                }
            }
        });

    }

    private boolean validateTrip() {

        if (name.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Your trip must have a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (strsource == null || strsource.trim().equals("")) {
            Toast.makeText(this, "Your trip must have a source", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (strdestination == null || strdestination.trim().equals("")) {
            Toast.makeText(this, "Your trip must have a destination", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            Toast.makeText(AddTrip.this, "Time specified already passed", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.mLayout).requestFocus();
    }
}

